package cc.dreamcode.command;

import lombok.Data;

@Data
public class CommandMeta {

    private final CommandContext commandContext;
    private final CommandExecutor commandExecutor;
}
