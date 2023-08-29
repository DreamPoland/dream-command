package cc.dreamcode.command;

import lombok.NonNull;

public interface DreamCommandSender<T> {

    void sendMessage(@NonNull String text);

    boolean hasPermission(@NonNull String permission);

    T getConsumer();
}
