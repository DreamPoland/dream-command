package cc.dreamcode.command.handler.type;

import cc.dreamcode.command.DreamCommandSender;
import cc.dreamcode.command.handler.CommandHandler;
import cc.dreamcode.command.handler.HandlerType;
import lombok.NonNull;

public interface NoPermissionType extends CommandHandler {

    @Override
    default HandlerType getHandlerType() {
        return HandlerType.NO_PERMISSION;
    }

    void handle(@NonNull DreamCommandSender<?> sender, @NonNull String permission);
}
