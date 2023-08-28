package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.annotation.Usage;
import cc.dreamcode.command.exception.CommandException;
import cc.dreamcode.command.exception.NoSuchCommandPathException;
import cc.dreamcode.command.extension.ExtensionManager;
import cc.dreamcode.command.path.CommandPath;
import cc.dreamcode.command.shared.AnnotationUtil;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public interface DreamCommandExecutor {
    default void invokeMethod(@NonNull ExtensionManager extensionManager, @NonNull CommandPath commandPath) {
        for (Method declaredMethod : Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.getAnnotation(Path.class) != null)
                .collect(Collectors.toList())) {

            if (!commandPath.isValid(declaredMethod)) {
                continue;
            }

            declaredMethod.setAccessible(true);

            final int annotationCount = (int) AnnotationUtil.countAnnotation(declaredMethod, Arg.class);

            final String[] argsRaw = new String[annotationCount];
            System.arraycopy(commandPath.getArguments(), 1, argsRaw, 0, annotationCount);

            final Object[] invokeObjects = new Object[argsRaw.length];
            for (int indexRaw = 0; indexRaw < argsRaw.length; indexRaw++) {
                final Class<?> objectClass = declaredMethod.getParameterTypes()[indexRaw];
                final Object object = extensionManager.resolveObject(objectClass, argsRaw[indexRaw])
                        .orElseThrow(() -> new CommandException("Cannot find extension resolver for class " + objectClass.getSimpleName()));

                invokeObjects[indexRaw] = object;
            }

            try {
                declaredMethod.invoke(this, invokeObjects);
                return;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new CommandException("Cannot invoke path method", e);
            }
        }

        final int arguments = commandPath.getArguments().length;
        final AtomicReference<Method> usageDefaultFallback = new AtomicReference<>();
        final AtomicReference<Method> usageFallback = new AtomicReference<>();
        for (Method usageMethod : Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.getAnnotation(Usage.class) != null)
                .collect(Collectors.toList())) {

            final Usage usage = usageMethod.getAnnotation(Usage.class);
            if (usage.arg() == 0) {
                usageDefaultFallback.set(usageMethod);
            }

            if (usage.arg() == arguments) {
                if (arguments == 0) {
                    break;
                }

                usageFallback.set(usageMethod);
            }
        }

        if (usageFallback.get() == null && usageDefaultFallback.get() == null) {
            throw new NoSuchCommandPathException(commandPath);
        }

        final Method usageMethod = usageFallback.get() != null
                ? usageFallback.get()
                : usageDefaultFallback.get();

        if (usageMethod != null) {
            try {
                usageMethod.invoke(this);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new CommandException("Cannot invoke usage method", e);
            }
        }
    }
}
