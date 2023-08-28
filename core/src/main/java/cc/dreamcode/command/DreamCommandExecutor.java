package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Usage;
import cc.dreamcode.command.exception.CommandException;
import cc.dreamcode.command.exception.NoSuchCommandPathException;
import cc.dreamcode.command.extension.ExtensionManager;
import cc.dreamcode.command.path.CommandPath;
import cc.dreamcode.command.shared.AnnotationUtil;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

public interface DreamCommandExecutor {
    default boolean invokeMethod(@NonNull ExtensionManager extensionManager, @NonNull CommandPath commandPath) {
        final int arguments = commandPath.getArguments().length;

        final AtomicReference<Method> usageDefaultFallback = new AtomicReference<>();
        final AtomicReference<Method> usageFallback = new AtomicReference<>();
        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {

            if (!commandPath.isValid(declaredMethod)) {
                final Usage usage = declaredMethod.getAnnotation(Usage.class);
                if (usage == null) {
                    continue;
                }

                if (usage.arg() == 0) {
                    usageDefaultFallback.set(declaredMethod);
                }

                if (usage.arg() == arguments) {
                    if (arguments == 0) {
                        break;
                    }

                    usageFallback.set(declaredMethod);
                }

                continue;
            }

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
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(this, invokeObjects);
                return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new CommandException("Cannot invoke path method", e);
            }
        }

        if (usageFallback.get() == null && usageDefaultFallback.get() == null) {
            throw new NoSuchCommandPathException(commandPath);
        }

        final Method usageMethod = usageFallback.get() != null
                ? usageFallback.get()
                : usageDefaultFallback.get();

        try {
            usageMethod.setAccessible(true);
            usageMethod.invoke(this);
            return false;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CommandException("Cannot invoke usage method", e);
        }
    }
}
