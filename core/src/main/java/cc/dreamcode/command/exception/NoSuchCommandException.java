package cc.dreamcode.command.exception;

import cc.dreamcode.command.DreamCommandValidator;
import lombok.NonNull;

public class NoSuchCommandException extends RuntimeException {

    public NoSuchCommandException(@NonNull DreamCommandValidator dreamCommandValidator) {
        super("Cannot find any command with path: " + dreamCommandValidator);
    }
}
