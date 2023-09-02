package cc.dreamcode.command.handler.type;

import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.command.handler.CommandHandler;
import cc.dreamcode.command.handler.HandlerType;
import lombok.NonNull;

public interface InvalidInputValueType extends CommandHandler {

    @Override
    default HandlerType getHandlerType() {
        return HandlerType.INVALID_INPUT_VALUE;
    }

    void handle(@NonNull DreamSender<?> sender, @NonNull Class<?> requiredClass, @NonNull String argument, int index);
}
