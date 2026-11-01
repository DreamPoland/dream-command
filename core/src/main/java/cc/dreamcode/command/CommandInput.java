package cc.dreamcode.command;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandInput {

    private final String label;
    private final String[] arguments;
    private final boolean spaceAtTheEnd;

    public CommandInput(@NonNull String label, @NonNull String[] arguments, boolean spaceAtTheEnd) {
        this.label = label;
        this.spaceAtTheEnd = spaceAtTheEnd;

        final List<String> argumentList = new ArrayList<>(arguments.length);
        for (String argument : arguments) {
            if (argument != null && !argument.isEmpty()) {
                argumentList.add(argument);
            }
        }
        this.arguments = argumentList.toArray(new String[0]);
    }

    public CommandInput(@NonNull String input) {
        String cleanInput = input;
        while (cleanInput.startsWith("/")) {
            cleanInput = cleanInput.substring(1);
        }

        this.spaceAtTheEnd = input.endsWith(" ");

        final String[] params = cleanInput.split(" ", -1);
        final List<String> paramList = new ArrayList<>();
        for (String param : params) {
            if (!param.isEmpty()) {
                paramList.add(param);
            }
        }

        if (paramList.isEmpty()) {
            throw new RuntimeException("Parse params cannot be empty (input)");
        }

        this.label = paramList.get(0);
        paramList.remove(0);
        this.arguments = paramList.toArray(new String[0]);
    }

    public String[] getParams() {
        final String[] params = new String[this.arguments.length + 1];
        params[0] = this.label;
        System.arraycopy(this.arguments, 0, params, 1, this.arguments.length);
        return params;
    }
}
