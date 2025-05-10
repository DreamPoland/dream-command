package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Command;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CommandEntry {

    private final String name;
    private final String[] aliases;
    private final String description;

    public CommandEntry(@NonNull Command command) {
        this.name = command.name();
        this.aliases = command.aliases();
        this.description = command.description();
    }
}
