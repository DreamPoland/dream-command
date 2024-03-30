package cc.dreamcode.command;

import lombok.Data;

@Data
public class CommandArgument {

    private final Type type;
    private final String value;

    enum Type {
        ARG, OPTIONAL_ARG, ARGS
    }
}
