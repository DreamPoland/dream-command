package cc.dreamcode.command;

import lombok.NonNull;

public interface DreamSender<T> {

    Type getType();

    String getName();

    boolean hasPermission(@NonNull String permission);

    T getHandler();

    enum Type {
        CONSOLE, CLIENT
    }
}
