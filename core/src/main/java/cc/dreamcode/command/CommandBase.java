package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.OptArg;
import cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.utilities.object.Duo;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CommandBase {

    default List<CommandPathMeta> getCommandPaths(@NonNull CommandMeta commandMeta) {
        final ListBuilder<CommandPathMeta> executors = new ListBuilder<>();

        Map<String, Duo<Integer, Integer>> usedPaths = new HashMap<>();
        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {
            declaredMethod.setAccessible(true);

            final Executor executor = declaredMethod.getAnnotation(Executor.class);
            if (executor == null) {
                continue;
            }

            for (String path : executor.path()) {

                int totalArgs = 0;
                int totalOptArgs = 0;
                for (Annotation[] parameterAnnotation : declaredMethod.getParameterAnnotations()) {
                    for (Annotation annotation : parameterAnnotation) {
                        if (Arg.class.isAssignableFrom(annotation.annotationType())) {
                            totalArgs++;
                        }

                        if (OptArg.class.isAssignableFrom(annotation.annotationType())) {
                            totalOptArgs++;
                        }
                    }
                }

                final Duo<Integer, Integer> value = usedPaths.get(path);
                if (value != null && value.getFirst() == totalArgs && value.getSecond() == totalOptArgs) {
                    throw new RuntimeException("duplicate path provided: " + path);
                }

                usedPaths.put(path, Duo.of(totalArgs, totalOptArgs));
            }
        }

        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {
            final Executor executor = declaredMethod.getAnnotation(Executor.class);
            if (executor == null) {
                continue;
            }

            for (String path : executor.path()) {
                final CommandPathMeta commandPathMeta = new CommandPathMeta(commandMeta, declaredMethod, path, executor.description());
                executors.add(commandPathMeta);
            }
        }

        return executors.build();
    }
}
