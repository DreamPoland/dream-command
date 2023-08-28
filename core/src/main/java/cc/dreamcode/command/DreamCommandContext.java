package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Command;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class DreamCommandContext {

    private final String label;
    private final String description;
    private final List<String> aliases;

    public static DreamCommandContext of(@NonNull Command command) {
        return new DreamCommandContext(command.label(), command.description(), Arrays.stream(command.aliases())
                .collect(Collectors.toList()));
    }
}
