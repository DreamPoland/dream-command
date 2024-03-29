package cc.dreamcode.command.handler.exception;

import cc.dreamcode.command.DreamSender;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class InvalidSenderException extends RuntimeException {

    private final List<DreamSender.Type> requireType;

    public InvalidSenderException(@NonNull List<DreamSender.Type> requireType, @NonNull String cause) {
        super(cause);

        this.requireType = requireType;
    }
}
