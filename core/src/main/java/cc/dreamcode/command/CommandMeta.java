package cc.dreamcode.command;

import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommandMeta {

    private final CommandContext commandContext;
    private final CommandBase commandBase;
    private final List<CommandExecutor> commandExecutors;

    public CommandMeta(@NonNull CommandContext commandContext, @NonNull CommandBase commandBase) {
        this.commandContext = commandContext;
        this.commandBase = commandBase;
        this.commandExecutors = commandBase.getMethods().entrySet()
                .stream()
                .map(entry -> new CommandExecutor(this, entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());
    }
}
