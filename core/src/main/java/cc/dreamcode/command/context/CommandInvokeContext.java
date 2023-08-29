package cc.dreamcode.command.context;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class CommandInvokeContext {

    private final String label;
    private final String[] arguments;

    public CommandInvokeContext(@NonNull String rawInput) {
        final String[] split = rawInput.split(" ");
        if (split.length == 0) {
            throw new RuntimeException("Cannot find require label in CommandPath input: " + rawInput);
        }

        this.label = split[0];

        final String[] arguments = new String[split.length - 1];
        System.arraycopy(split, 1, arguments, 0, split.length - 1);

        this.arguments = arguments;
    }
}
