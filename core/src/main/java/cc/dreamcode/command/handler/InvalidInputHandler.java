package cc.dreamcode.command.handler;

import cc.dreamcode.command.CommandSender;
import lombok.NonNull;

public interface InvalidInputHandler {

    void handle(@NonNull CommandSender<?> commandSender, @NonNull Class<?> requiringClass, @NonNull String input);
}
