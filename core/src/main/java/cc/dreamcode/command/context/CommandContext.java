package cc.dreamcode.command.context;

import cc.dreamcode.command.annotation.Command;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class CommandContext {

    private final String label;
    private final String description;
    private final String[] aliases;

    public static CommandContext of(@NonNull Command command) {
        return CommandContext.of(command.label(), command.description(), command.aliases());
    }
}
