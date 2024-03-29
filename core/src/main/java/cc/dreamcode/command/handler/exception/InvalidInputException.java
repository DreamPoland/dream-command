package cc.dreamcode.command.handler.exception;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class InvalidInputException extends RuntimeException {

    private final Class<?> requiringClass;
    private final String input;

    public InvalidInputException(@NonNull Class<?> requiringClass, @NonNull String input, @NonNull String cause) {
        super(cause);

        this.requiringClass = requiringClass;
        this.input = input;
    }
}
