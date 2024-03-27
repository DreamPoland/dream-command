package cc.dreamcode.command;

import lombok.NonNull;

public interface CommandSender<T> {

    String getName();

    boolean hasPermission(@NonNull String permission);

    T getHandler();
}
