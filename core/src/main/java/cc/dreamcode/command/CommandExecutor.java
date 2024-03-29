package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.OptArg;
import cc.dreamcode.command.bind.BindService;
import cc.dreamcode.command.resolver.ResolverService;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.Data;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class CommandExecutor {

    private final CommandMeta commandMeta;

    private final Method method;
    private final Map<Integer, Annotation[]> paramAnnotations;
    private final Map<Integer, Class<?>> paramArgs;
    private final Map<Integer, Class<?>> paramMultiArgs;
    private final Map<Integer, Class<Optional<?>>> paramOptionalArgs;
    private final Map<Integer, Class<?>> paramBinds;

    private final String path;
    private final String description;

    @SuppressWarnings("unchecked")
    public CommandExecutor(@NonNull CommandMeta commandMeta, @NonNull Method method, @NonNull Executor executor) {
        this.commandMeta = commandMeta;
        this.method = method;

        this.paramAnnotations = new HashMap<>();
        for (int index = 0; index < this.method.getParameterAnnotations().length; index++) {
            this.paramAnnotations.put(index, this.method.getParameterAnnotations()[index]);
        }
        
        this.paramArgs = new HashMap<>();
        this.paramMultiArgs = new HashMap<>();
        this.paramOptionalArgs = new HashMap<>();
        this.paramBinds = new HashMap<>();
        for (int index = 0; index < this.method.getParameterTypes().length; index++) {

            if (Arrays.stream(this.paramAnnotations.get(index))
                    .anyMatch(annotation -> Arg.class.isAssignableFrom(annotation.annotationType()))) {

                // arg (transformer)
                this.paramArgs.put(index, this.method.getParameterTypes()[index]);
                continue;
            }

            if (Arrays.stream(this.paramAnnotations.get(index))
                    .anyMatch(annotation -> Args.class.isAssignableFrom(annotation.annotationType()))) {

                // multi-arg (transformer)
                this.paramMultiArgs.put(index, this.method.getParameterTypes()[index]);
                continue;
            }

            if (Arrays.stream(this.paramAnnotations.get(index))
                    .anyMatch(annotation -> OptArg.class.isAssignableFrom(annotation.annotationType()))) {

                int finalIndex = index;
                if (this.paramArgs.keySet()
                        .stream()
                        .anyMatch(argIndex -> argIndex > finalIndex)) {
                    throw new RuntimeException("@OptionalArg must be specified after @Arg params");
                }

                this.paramOptionalArgs.put(index, (Class<Optional<?>>) this.method.getParameterTypes()[index]);
                continue;
            }

            // bind
            this.paramBinds.put(index, this.method.getParameterTypes()[index]);
        }

        this.path = executor.path();
        this.description = executor.description();
    }

    public void invoke(@NonNull ResolverService resolverService, @NonNull BindService bindService, @NonNull CommandInput commandInput, @NonNull CommandSender<?> sender) throws InvocationTargetException, IllegalAccessException {

        final ListBuilder<Object> objects = new ListBuilder<>();
        final int patterns = this.path.isEmpty() ? 0 : this.path.split(" ").length;

        final String[] params = new String[commandInput.getArguments().length - patterns];
        System.arraycopy(commandInput.getArguments(), patterns, params, 0, params.length);

        final AtomicInteger atomicArg = new AtomicInteger();
        for (int index = 0; index < this.method.getParameterCount(); index++) {

            if (this.paramArgs.containsKey(index)) {
                final String input = params[atomicArg.get()];
                final Class<?> paramType = new ArrayList<>(this.paramArgs.values()).get(atomicArg.get());

                final Optional<?> optionalObject = resolverService.resolve(paramType, input);
                if (!optionalObject.isPresent()) {
                    throw new RuntimeException("Cannot resolve param " + input + " as a " + paramType.getSimpleName());
                }

                objects.add(optionalObject.get());
                atomicArg.incrementAndGet();
                continue;
            }

            if (this.paramOptionalArgs.containsKey(index)) {

                final Class<?> paramType = this.paramOptionalArgs.get(atomicArg.get());
                if (params.length <= atomicArg.get()) {
                    objects.add(Optional.class.isAssignableFrom(paramType) ? Optional.empty() : null);
                    atomicArg.incrementAndGet();
                    continue;
                }

                final Optional<Annotation> optionalAnnotation = Arrays.stream(this.paramAnnotations.get(index))
                        .filter(annotation -> annotation.annotationType().equals(OptArg.class))
                        .findAny();

                if (!optionalAnnotation.isPresent()) {
                    throw new RuntimeException("Annotation @OptArg not found (critical bug)");
                }

                final OptArg optArg = (OptArg) optionalAnnotation.get();
                final String input = params[atomicArg.get()];

                if (Optional.class.isAssignableFrom(paramType)) {
                    final Class<?> optionalType = optArg.generic();
                    if (optionalType.equals(Class.class)) {
                        throw new RuntimeException("Optional requires generic argument in @OptArg annotation");
                    }

                    final Optional<?> optionalObject = resolverService.resolve(optionalType, input);
                    if (!optionalObject.isPresent()) {
                        throw new RuntimeException("Cannot resolve optional-param " + input + " as a " + optionalType.getSimpleName());
                    }

                    objects.add(optionalObject);
                    atomicArg.incrementAndGet();
                    continue;
                }

                final Optional<?> optionalObject = resolverService.resolve(paramType, input);
                if (!optionalObject.isPresent()) {
                    throw new RuntimeException("Cannot resolve optional-param " + input + " as a " + paramType.getSimpleName());
                }

                objects.add(optionalObject.get());
                atomicArg.incrementAndGet();
                continue;
            }

            if (this.paramMultiArgs.containsKey(index)) {
                final Class<?> paramType = this.paramMultiArgs.get(index);
                final Optional<Annotation> optionalAnnotation = Arrays.stream(this.paramAnnotations.get(index))
                        .filter(annotation -> annotation.annotationType().equals(Args.class))
                        .findAny();

                if (!optionalAnnotation.isPresent()) {
                    throw new RuntimeException("Annotation @Args not found (critical bug)");
                }

                final Args args = (Args) optionalAnnotation.get();
                final String skip = StringUtil.join(params, " ",
                        args.min() == -1 ? 0 : Math.min(args.min(), params.length),
                        args.max() == -1 ? params.length : Math.min(args.max(), params.length));

                final Object[] array = Arrays.stream(skip.split(" "))
                        .map(input -> {

                            final Optional<?> optionalObject = resolverService.resolve(paramType.getComponentType(), input);
                            if (!optionalObject.isPresent()) {
                                throw new RuntimeException("Cannot resolve param " + input + " as a " + paramType.getSimpleName());
                            }

                            return optionalObject.get();
                        })
                        .toArray();

                objects.add(resolverService.resolveArray(paramType, array)
                        .orElseThrow(() -> new RuntimeException("Cannot resolve array: " + paramType)));
                continue;
            }

            if (this.paramBinds.containsKey(index)) {
                final Class<?> paramType = this.paramBinds.get(index);
                final Optional<?> optionalObject = bindService.resolveBind(paramType, sender);

                if (!optionalObject.isPresent()) {
                    throw new RuntimeException("Cannot resolve bind: " + paramType.getSimpleName());
                }

                objects.add(optionalObject.get());
            }
        }

        this.method.invoke(this.commandMeta.getCommandBase(), objects.build().toArray());
    }
}
