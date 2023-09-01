package cc.dreamcode.command.handler.type;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.DreamCommandSender;
import cc.dreamcode.command.context.CommandInvokeContext;
import cc.dreamcode.command.context.CommandPathContext;
import cc.dreamcode.command.handler.CommandHandler;
import cc.dreamcode.command.handler.HandlerType;
import lombok.NonNull;

import java.util.List;

public interface InvalidUsageType extends CommandHandler {

    @Override
    default HandlerType getHandlerType() {
        return HandlerType.INVALID_USAGE;
    }

    void handle(@NonNull DreamCommandSender<?> sender, @NonNull DreamCommandExecutor executor, @NonNull List<CommandPathContext> commandPathContextList, @NonNull CommandInvokeContext commandInvokeContext);
}
