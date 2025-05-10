package cc.dreamcode.command;

import lombok.Data;

@Data
public class ArgumentEntry {

    private final Type type;
    private final String value;

    enum Type {
        ARG, OPTIONAL_ARG, ARGS
    }
}
