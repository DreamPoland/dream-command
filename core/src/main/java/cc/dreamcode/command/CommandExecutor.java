package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.bind.BindService;
import cc.dreamcode.command.handler.exception.InvalidInputException;
import cc.dreamcode.command.handler.exception.InvalidPermissionException;
import cc.dreamcode.command.handler.exception.InvalidSenderException;
import cc.dreamcode.command.resolver.ResolverService;
import cc.dreamcode.command.result.ResultService;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.utilities.object.Duo;
import lombok.Data;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class CommandExecutor {

    private final CommandMeta commandMeta;
    private final CommandPathMeta commandPathMeta;

    public void execute(@NonNull CommandScheduler commandScheduler, @NonNull ResolverService resolverService, @NonNull BindService bindService, @NonNull ResultService resultService, @NonNull DreamSender<?> sender, @NonNull CommandInput commandInput) {

        final List<DreamSender.Type> senderTypes = this.commandPathMeta.getSendersType();
        if (!senderTypes.isEmpty() && !senderTypes.contains(sender.getType())) {
            throw new InvalidSenderException(senderTypes, "Sender type is unacceptable (" + sender.getType() + ")");
        }

        for (String permission : this.commandPathMeta.getPermissions()) {
            if (!sender.hasPermission(permission)) {
                throw new InvalidPermissionException(permission, "Sender permission not found (" + permission + ")");
            }
        }

        final String path = this.commandPathMeta.getPath();

        final ListBuilder<Object> objects = new ListBuilder<>();
        final int pathLength = path.isEmpty() ? 0 : path.split(" ").length;

        final String[] params = new String[commandInput.getArguments().length - pathLength];
        System.arraycopy(commandInput.getArguments(), pathLength, params, 0, params.length);

        final AtomicInteger atomicArg = new AtomicInteger();
        for (int index = 0; index < this.commandPathMeta.getMethod().getParameterCount(); index++) {

            if (this.getCommandPathMeta().getParamArgs().containsKey(index)) {
                final String input = params[atomicArg.get()];
                final Class<?> paramType = new ArrayList<>(this.getCommandPathMeta().getParamArgs().values()).get(atomicArg.get());

                final Optional<?> optionalObject = resolverService.resolve(paramType, input);
                if (!optionalObject.isPresent()) {
                    throw new InvalidInputException(paramType, input, "Cannot resolve param " + input + " as a " + paramType.getSimpleName());
                }

                objects.add(optionalObject.get());
                atomicArg.incrementAndGet();
                continue;
            }

            if (this.getCommandPathMeta().getParamOptionalArgs().containsKey(index)) {

                final Duo<Class<?>, Boolean> paramOptDuo = this.getCommandPathMeta().getParamOptionalArgs().get(index);
                final Class<?> rawType = paramOptDuo.getFirst();
                final boolean optional = paramOptDuo.getSecond();

                if (params.length <= atomicArg.get()) {
                    objects.add(optional ? Optional.empty() : null);
                    atomicArg.incrementAndGet();
                    continue;
                }

                final String input = params[atomicArg.get()];
                final Optional<?> optionalObject = resolverService.resolve(rawType, input);
                if (!optionalObject.isPresent()) {
                    throw new InvalidInputException(rawType, input, "Cannot resolve optional-param " + input + " as a " + rawType.getSimpleName());
                }

                objects.add(optional ? optionalObject : optionalObject.get());
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
                                throw new InvalidInputException(paramType, input, "Cannot resolve param " + input + " as a " + paramType.getSimpleName());
                            }

                            return optionalObject.get();
                        })
                        .toArray();

                objects.add(resolverService.resolveArray(paramType, array)
                        .orElseThrow(() -> new InvalidInputException(paramType, skip, "Cannot resolve part of array: " + paramType)));
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

        final Runnable invoke = () -> {
            try {
                Object object = this.commandPathMeta.getMethod().invoke(this.commandMeta.getCommandInstance(), objects.build().toArray());

                if (object != null) {
                    resultService.resolveResult(sender, object.getClass(), object);
                }
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Cannot invoke command-path /" + this.commandMeta.getCommandContext().getName() + " " + this.commandPathMeta.getPath(), e);
            }
        };

        if (this.commandPathMeta.isAsync()) {
            commandScheduler.async(invoke);
        }
        else {
            commandScheduler.sync(invoke);
        }
    }
}
