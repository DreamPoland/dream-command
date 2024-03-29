package cc.dreamcode.command.handler;

import cc.dreamcode.command.DreamSender;
import lombok.NonNull;

public interface InvalidInputHandler {

    void handle(@NonNull DreamSender<?> dreamSender, @NonNull Class<?> requiringClass, @NonNull String input);
}
