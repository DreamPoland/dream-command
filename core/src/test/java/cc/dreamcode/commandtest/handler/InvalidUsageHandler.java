package cc.dreamcode.commandtest.handler;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.DreamCommandSender;
import cc.dreamcode.command.DreamCommandValidator;
import cc.dreamcode.command.context.CommandInvokeContext;
import cc.dreamcode.command.context.CommandPathContext;
import cc.dreamcode.command.handler.type.InvalidUsage;
import cc.dreamcode.utilities.StringUtil;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InvalidUsageHandler implements InvalidUsage {
    @Override     // EXAMPLE
    public void handle(@NonNull DreamCommandSender<?> sender, @NonNull DreamCommandExecutor executor, @NonNull List<CommandPathContext> commandPathContextList, @NonNull CommandInvokeContext commandInvokeContext) {
        final StringBuilder usage = new StringBuilder("[ERR] Poprawne uzycie: /" + executor.getContext().getLabel());

        final DreamCommandValidator validator = new DreamCommandValidator(commandInvokeContext);
        final Optional<CommandPathContext> similarOptionalPath = validator.findSimilarPath(commandPathContextList);

        if (similarOptionalPath.isPresent()) {
            final CommandPathContext similarPath = similarOptionalPath.get();
            usage.append(" ").append(similarPath.getPathName());

            if (similarPath.getMethodArgs().length != 0) {
                usage.append(" (").append(StringUtil.join(similarPath.getMethodArgs(), ", ")).append(")");
            }
        }
        else {
            usage.append(" [").append(commandPathContextList.stream()
                    .map(CommandPathContext::getPathName)
                    .distinct()
                    .collect(Collectors.joining(", "))).append("]");
        }

        System.out.println("[ERR] Poprawne uzycie: " + usage);
    }
}
