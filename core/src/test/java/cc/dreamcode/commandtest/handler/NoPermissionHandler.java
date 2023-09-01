package cc.dreamcode.commandtest.handler;

import cc.dreamcode.command.DreamCommandSender;
import cc.dreamcode.command.handler.type.NoPermissionType;
import lombok.NonNull;

public class NoPermissionHandler implements NoPermissionType {
    @Override
    public void handle(@NonNull DreamCommandSender<?> sender, @NonNull String permission) {
        sender.sendMessage("Nie posiadasz uprawnien! (" + permission + ")");
    }
}
