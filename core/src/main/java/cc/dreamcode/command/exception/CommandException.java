package cc.dreamcode.command.exception;

import lombok.NonNull;

public class CommandException extends RuntimeException {

    public CommandException(@NonNull String text) {
        super(text);
    }

    public CommandException(@NonNull String text, @NonNull Throwable throwable) {
        super(text, throwable);
    }
}
