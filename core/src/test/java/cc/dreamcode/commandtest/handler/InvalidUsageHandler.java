package cc.dreamcode.commandtest.handler;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.context.CommandInvokeContext;
import cc.dreamcode.command.context.CommandPathContext;
import cc.dreamcode.command.handler.type.InvalidUsageType;
import cc.dreamcode.command.sender.DreamSender;
import lombok.NonNull;

import java.util.List;

public class InvalidUsageHandler implements InvalidUsageType {
    @Override     // EXAMPLE
    public void handle(@NonNull DreamSender<?> sender, @NonNull DreamCommandExecutor executor, @NonNull List<CommandPathContext> commandPathContextList, @NonNull CommandInvokeContext commandInvokeContext) {
        sender.sendMessage("[ERR] Poprawne uzycie: " + this.getDefaultUsage(sender, executor, commandPathContextList, commandInvokeContext));
    }
}
