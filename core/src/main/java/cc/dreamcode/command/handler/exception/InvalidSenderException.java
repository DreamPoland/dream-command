package cc.dreamcode.command.handler.exception;

import cc.dreamcode.command.CommandSender;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class InvalidSenderException extends RuntimeException {

    private final List<CommandSender.Type> requireType;

    public InvalidSenderException(@NonNull List<CommandSender.Type> requireType, @NonNull String cause) {
        super(cause);

        this.requireType = requireType;
    }
}
