package cc.dreamcode.command.handler.type;

import cc.dreamcode.command.handler.CommandHandler;
import cc.dreamcode.command.handler.HandlerType;
import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.command.sender.SenderType;
import lombok.NonNull;

public interface InvalidSenderType extends CommandHandler {

    @Override
    default HandlerType getHandlerType() {
        return HandlerType.INVALID_SENDER_TYPE;
    }

    void handle(@NonNull DreamSender<?> sender, @NonNull SenderType requiredSender);
}
