package cc.dreamcode.command.handler;

import cc.dreamcode.command.DreamSender;
import lombok.NonNull;

public interface InvalidPermissionHandler {

    void handle(@NonNull DreamSender<?> dreamSender, @NonNull String permission);
}
