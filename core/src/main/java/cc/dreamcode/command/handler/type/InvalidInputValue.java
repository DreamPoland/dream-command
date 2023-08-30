package cc.dreamcode.command.handler.type;

import cc.dreamcode.command.DreamCommandSender;
import cc.dreamcode.command.handler.CommandHandler;
import cc.dreamcode.command.handler.HandlerType;
import lombok.NonNull;

public interface InvalidInputValue extends CommandHandler {

    @Override
    default HandlerType getHandlerType() {
        return HandlerType.INVALID_INPUT_VALUE;
    }

    void handle(@NonNull DreamCommandSender<?> sender, @NonNull Class<?> requiredClass, @NonNull String argument);
}
