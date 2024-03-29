package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.List;

public interface CommandBase {

    default List<CommandPathMeta> getCommandPaths(@NonNull CommandMeta commandMeta) {
        final ListBuilder<CommandPathMeta> executors = new ListBuilder<>();

        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {
            declaredMethod.setAccessible(true);

            final Executor executor = declaredMethod.getAnnotation(Executor.class);
            if (executor == null) {
                continue;
            }

            final CommandPathMeta commandPathMeta = new CommandPathMeta(commandMeta, declaredMethod, executor);
            executors.add(commandPathMeta);
        }

        return executors.build();
    }
}
