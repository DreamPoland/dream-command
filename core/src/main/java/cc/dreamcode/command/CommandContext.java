package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Command;
import lombok.Data;
import lombok.NonNull;

@Data
public class CommandContext {

    private final String name;
    private final String[] aliases;
    private final String description;

    public CommandContext(@NonNull Command command) {
        this.name = command.name();
        this.aliases = command.aliases();
        this.description = command.description();
    }
}
