package cc.dreamcode.command.handler;

import cc.dreamcode.command.CommandSender;
import lombok.NonNull;

public interface InvalidPermissionHandler {

    void handle(@NonNull CommandSender<?> commandSender, @NonNull String permission);
}
