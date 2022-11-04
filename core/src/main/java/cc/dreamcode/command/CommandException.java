package cc.dreamcode.command;


import lombok.Getter;
import lombok.NonNull;

public final class CommandException extends RuntimeException {

    @Getter
    private final String message;

    public CommandException(@NonNull String message) {
        this.message = message;
    }
}
