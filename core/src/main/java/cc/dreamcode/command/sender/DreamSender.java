package cc.dreamcode.command.sender;

import lombok.NonNull;

public interface DreamSender<T> {

    SenderType getSenderType();

    String getName();

    void sendMessage(@NonNull String text);

    boolean hasPermission(@NonNull String permission);

    T getConsumer();
}
