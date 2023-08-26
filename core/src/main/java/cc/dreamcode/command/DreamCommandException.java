package cc.dreamcode.command;

import lombok.NonNull;

public class DreamCommandException extends RuntimeException {

    public DreamCommandException(@NonNull String text) {
        super(text);
    }

    public DreamCommandException(@NonNull String text, @NonNull Throwable throwable) {
        super(text, throwable);
    }
}
