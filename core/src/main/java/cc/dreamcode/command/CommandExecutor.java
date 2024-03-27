package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Executor;
import lombok.Data;
import lombok.NonNull;

import java.lang.reflect.Method;

@Data
public class CommandExecutor {

    private final CommandMeta commandMeta;

    private final Method method;
    private final String pattern;
    private final String description;

    public CommandExecutor(@NonNull CommandMeta commandMeta, @NonNull Method method, @NonNull Executor executor) {
        this.commandMeta = commandMeta;
        this.method = method;
        this.pattern = executor.pattern();
        this.description = executor.description();
    }
}
