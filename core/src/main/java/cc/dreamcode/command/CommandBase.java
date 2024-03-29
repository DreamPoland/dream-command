package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.List;

public interface CommandBase {

    default List<CommandExecutor> getExecutors(@NonNull CommandMeta commandMeta) {
        final ListBuilder<CommandExecutor> executors = new ListBuilder<>();

        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {
            declaredMethod.setAccessible(true);

            final Executor executor = declaredMethod.getAnnotation(Executor.class);
            if (executor == null) {
                continue;
            }

            final CommandExecutor commandExecutor = new CommandExecutor(commandMeta, declaredMethod, executor);
            executors.add(commandExecutor);
        }

        return executors.build();
    }
}
