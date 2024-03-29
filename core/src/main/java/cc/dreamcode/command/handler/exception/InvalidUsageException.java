package cc.dreamcode.command.handler.exception;

import cc.dreamcode.command.CommandInput;
import cc.dreamcode.command.CommandMeta;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class InvalidUsageException extends RuntimeException {

    private final CommandMeta commandMeta;
    private final CommandInput commandInput;

    public InvalidUsageException(CommandMeta commandMeta, @NonNull CommandInput commandInput, @NonNull String cause) {
        super(cause);

        this.commandMeta = commandMeta;
        this.commandInput = commandInput;
    }
}
