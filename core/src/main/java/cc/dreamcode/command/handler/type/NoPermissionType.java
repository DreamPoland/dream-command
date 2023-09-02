package cc.dreamcode.command.handler.type;

import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.command.handler.CommandHandler;
import cc.dreamcode.command.handler.HandlerType;
import lombok.NonNull;

public interface NoPermissionType extends CommandHandler {

    @Override
    default HandlerType getHandlerType() {
        return HandlerType.NO_PERMISSION;
    }

    void handle(@NonNull DreamSender<?> sender, @NonNull String permission);
}
