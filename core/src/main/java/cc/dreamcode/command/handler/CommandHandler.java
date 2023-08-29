package cc.dreamcode.command.handler;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.DreamCommandSender;
import cc.dreamcode.command.context.CommandPathContext;
import cc.dreamcode.command.context.CommandInvokeContext;
import lombok.NonNull;

import java.util.List;

public interface CommandHandler {

    void handle(@NonNull DreamCommandSender<?> sender, @NonNull DreamCommandExecutor executor, @NonNull List<CommandPathContext> commandPathContextList, @NonNull CommandInvokeContext commandInvokeContext);
}
