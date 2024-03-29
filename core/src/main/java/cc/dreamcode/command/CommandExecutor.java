package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.OptArg;
import cc.dreamcode.command.bind.BindService;
import cc.dreamcode.command.resolver.ResolverService;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.Data;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class CommandExecutor {

    private final CommandMeta commandMeta;
    private final CommandPathMeta commandPathMeta;

    public void invoke(@NonNull ResolverService resolverService, @NonNull BindService bindService, @NonNull CommandInput commandInput, @NonNull CommandSender<?> sender) throws InvocationTargetException, IllegalAccessException {

        if (!this.commandPathMeta.getSendersType().isEmpty()) {
            if (!this.commandPathMeta.getSendersType().contains(sender.getType())) {
                throw new RuntimeException("Sender type is unacceptable (" + sender.getType() + ")");
            }
        }

        for (String permission : this.commandPathMeta.getPermissions()) {
            if (!sender.hasPermission(permission)) {
                throw new RuntimeException("Sender permission not found (" + permission + ")");
            }
        }

        final String path = this.commandPathMeta.getPath();

        final ListBuilder<Object> objects = new ListBuilder<>();
        final int patterns = path.isEmpty() ? 0 : path.split(" ").length;

        final String[] params = new String[commandInput.getArguments().length - patterns];
        System.arraycopy(commandInput.getArguments(), patterns, params, 0, params.length);

        final AtomicInteger atomicArg = new AtomicInteger();
        for (int index = 0; index < this.commandPathMeta.getMethod().getParameterCount(); index++) {

            if (this.getCommandPathMeta().getParamArgs().containsKey(index)) {
                final String input = params[atomicArg.get()];
                final Class<?> paramType = new ArrayList<>(this.getCommandPathMeta().getParamArgs().values()).get(atomicArg.get());

                final Optional<?> optionalObject = resolverService.resolve(paramType, input);
                if (!optionalObject.isPresent()) {
                    throw new RuntimeException("Cannot resolve param " + input + " as a " + paramType.getSimpleName());
                }

                objects.add(optionalObject.get());
                atomicArg.incrementAndGet();
                continue;
            }

            if (this.getCommandPathMeta().getParamOptionalArgs().containsKey(index)) {

                final Class<?> paramType = this.getCommandPathMeta().getParamOptionalArgs().get(atomicArg.get());
                if (params.length <= atomicArg.get()) {
                    objects.add(Optional.class.isAssignableFrom(paramType) ? Optional.empty() : null);
                    atomicArg.incrementAndGet();
                    continue;
                }

                final Optional<Annotation> optionalAnnotation = Arrays.stream(this.getCommandPathMeta().getParamAnnotations().get(index))
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

            if (this.commandPathMeta.getParamMultiArgs().containsKey(index)) {
                final Class<?> paramType = this.commandPathMeta.getParamMultiArgs().get(index);
                final Optional<Annotation> optionalAnnotation = Arrays.stream(this.commandPathMeta.getParamAnnotations().get(index))
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

            if (this.commandPathMeta.getParamBinds().containsKey(index)) {
                final Class<?> paramType = this.commandPathMeta.getParamBinds().get(index);
                final Optional<?> optionalObject = bindService.resolveBind(paramType, sender);

                if (!optionalObject.isPresent()) {
                    throw new RuntimeException("Cannot resolve bind: " + paramType.getSimpleName());
                }

                objects.add(optionalObject.get());
            }
        }

        this.commandPathMeta.getMethod().invoke(this.commandMeta.getCommandBase(), objects.build().toArray());
    }
}
