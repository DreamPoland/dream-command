package cc.dreamcode.command.handler.exception;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class InvalidPermissionException extends RuntimeException {

    private final String permission;

    public InvalidPermissionException(@NonNull String permission, @NonNull String cause) {
        super(cause);

        this.permission = permission;
    }
}
