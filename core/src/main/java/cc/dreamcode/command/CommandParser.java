package cc.dreamcode.command;

import lombok.Data;
import lombok.NonNull;

@Data
public class CommandParser {

    private final String input;

    public CommandParser(@NonNull String input) {
        this.input = input.replace("/", "");
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
}
