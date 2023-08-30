package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.context.CommandContext;
import cc.dreamcode.command.context.CommandInvokeContext;
import cc.dreamcode.command.context.CommandPathContext;
import cc.dreamcode.command.exception.CommandException;
import cc.dreamcode.command.extension.ExtensionManager;
import cc.dreamcode.command.handler.HandlerManager;
import cc.dreamcode.command.handler.HandlerType;
import cc.dreamcode.command.handler.type.InvalidInputValue;
import cc.dreamcode.command.handler.type.InvalidUsage;
import cc.dreamcode.command.shared.AnnotationUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Setter
@NoArgsConstructor
public abstract class DreamCommandExecutor {

    private @Getter CommandContext context;
    private ExtensionManager extensionManager;
    private HandlerManager handlerManager;

    public boolean invokeMethod(@NonNull DreamCommandSender<?> sender, @NonNull CommandInvokeContext commandInvokeContext) {
        final DreamCommandValidator validator = new DreamCommandValidator(commandInvokeContext);
        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {

            if (!validator.isSimilar(this.context, declaredMethod)) {
                continue;
            }

            final CommandPathContext commandPathContext = new CommandPathContext(this.context, declaredMethod);
            final String usingPath = validator.getUsingPathName(commandPathContext)
                    .orElseThrow(() -> new CommandException("Cannot find using path by context: " + commandInvokeContext));

            final AtomicInteger otherParams = new AtomicInteger();
            final Object[] invokeObjects = new Object[declaredMethod.getParameterCount()];
            final String[] invokeArgs = new String[commandPathContext.getMethodArgs().length];
            System.arraycopy(commandInvokeContext.getArguments(), usingPath.split(" ").length, invokeArgs, 0, invokeArgs.length);

            for (int indexRaw = 0; indexRaw < declaredMethod.getParameterCount(); indexRaw++) {
                final Class<?> objectClass = declaredMethod.getParameterTypes()[indexRaw];

                if (DreamCommandSender.class.isAssignableFrom(objectClass)) {
                    invokeObjects[indexRaw] = sender;
                    otherParams.incrementAndGet();
                    continue;
                }

                final String input = invokeArgs[indexRaw - otherParams.get()];
                try {
                    invokeObjects[indexRaw] = this.extensionManager.resolveObject(objectClass, input)
                            .orElseThrow(() -> new CommandException("Cannot find extension resolver for class " + objectClass.getSimpleName()));
                }
                catch (IllegalArgumentException e) {
                    final int finalIndexRaw = indexRaw;
                    this.handlerManager.getCommandHandler(HandlerType.INVALID_INPUT_VALUE).ifPresent(commandHandler -> {
                        final InvalidInputValue invalidInputValue = (InvalidInputValue) commandHandler;
                        invalidInputValue.handle(sender, objectClass, input, finalIndexRaw);
                    });

                    return false;
                }
            }

            try {
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(this, invokeObjects);
                return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new CommandException("Cannot invoke path method", e);
            }
        }

        this.handlerManager.getCommandHandler(HandlerType.INVALID_USAGE).ifPresent(commandHandler -> {
            final InvalidUsage invalidUsage = (InvalidUsage) commandHandler;
            invalidUsage.handle(sender, this, this.getCommandPathList(), commandInvokeContext);
        });
        return false;
    }

    public List<CommandPathContext> getCommandPathList() {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.getAnnotation(Path.class) != null)
                .map(method -> {
                    final Path path = method.getAnnotation(Path.class);
                    return new CommandPathContext(this.context, path, AnnotationUtil.getAnnotation(method, Arg.class));
                })
                .collect(Collectors.toList());
    }
}
