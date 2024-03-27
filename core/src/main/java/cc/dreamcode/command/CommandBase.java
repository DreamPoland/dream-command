package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.utilities.builder.MapBuilder;

import java.lang.reflect.Method;
import java.util.Map;

public interface CommandBase {

    default Map<Executor, Method> getMethods() {
        final MapBuilder<Executor, Method> commandMethods = new MapBuilder<>();

        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {

            final Executor executor = declaredMethod.getAnnotation(Executor.class);
            if (executor == null) {
                continue;
            }

            commandMethods.put(executor, declaredMethod);
        }

        return commandMethods.build();
    }
}
