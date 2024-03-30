package cc.dreamcode.command;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommandInput {

    private final String label;
    private final String[] arguments;
    private final boolean spaceAtTheEnd;

    public CommandInput(@NonNull String input) {

        final String[] params = input.replace("/", "").split(" ");
        if (params.length == 0) {
            throw new RuntimeException("Parse params cannot be empty (input)");
        }

        this.label = params[0];

        this.arguments = new String[params.length - 1];
        System.arraycopy(params, 1, this.arguments, 0, this.arguments.length);

        this.spaceAtTheEnd = input.endsWith(" ");
    }

    public String[] getParams() {

        final String[] params = new String[this.arguments.length + 1];

        params[0] = this.label;
        System.arraycopy(this.arguments, 0, params, 1, this.arguments.length);

        return params;
    }
}
