package cc.dreamcode.command;

import cc.dreamcode.utilities.StringUtil;
import lombok.Getter;
import lombok.NonNull;

public class CommandInput {

    private final String input;
    @Getter private final boolean spaceAtTheEnd;

    public CommandInput(@NonNull String input) {
        this.input = input.replace("/", "");
        this.spaceAtTheEnd = input.endsWith(" ");
    }

    public CommandInput(@NonNull String label, @NonNull String[] args, boolean spaceAtTheEnd) {
        this.input = label + " " + StringUtil.join(args, " ");
        this.spaceAtTheEnd = spaceAtTheEnd;
    }

    public String[] getParams() {
        return this.input.split(" ");
    }

    public String getLabel() {

        final String[] params = this.getParams();
        if (params.length == 0) {
            throw new RuntimeException("Parse params cannot be empty (input)");
        }

        return params[0];
    }

    public String[] getArguments() {

        final String[] params = this.getParams();
        final String[] arguments = new String[params.length - 1];

        System.arraycopy(params, 1, arguments, 0, arguments.length);
        return arguments;
    }
}
