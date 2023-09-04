package cc.dreamcode.commandtest.handler;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.command.DreamCommandValidator;
import cc.dreamcode.command.context.CommandInvokeContext;
import cc.dreamcode.command.context.CommandPathContext;
import cc.dreamcode.command.handler.type.InvalidUsageType;
import cc.dreamcode.utilities.StringUtil;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InvalidUsageHandler implements InvalidUsageType {
    @Override     // EXAMPLE
    public void handle(@NonNull DreamSender<?> sender, @NonNull DreamCommandExecutor executor, @NonNull List<CommandPathContext> commandPathContextList, @NonNull CommandInvokeContext commandInvokeContext) {
        final StringBuilder usage = new StringBuilder("/" + executor.getContext().getLabel());

        final DreamCommandValidator validator = new DreamCommandValidator(commandInvokeContext);
        final Optional<CommandPathContext> similarOptionalPath = commandPathContextList.stream()
                .filter(validator::canBeSuggestion)
                .findAny();

        if (similarOptionalPath.isPresent()) {
            final CommandPathContext similarPath = similarOptionalPath.get();
            if (!similarPath.getPathName().isEmpty()) {
                usage.append(" ").append(similarPath.getPathName());
            }

            if (!similarPath.getMethodArgs().isEmpty() || !similarPath.getMethodArgsRow().isEmpty()) {
                usage.append(" (").append(StringUtil.join(similarPath.getMethodArgsNames(), ", "))
                        .append(StringUtil.join(similarPath.getMethodArgsRowNames(), ", ")).append(")");
            }
        }
        else {
            usage.append(" [").append(commandPathContextList.stream()
                    .map(CommandPathContext::getPathName)
                    .distinct()
                    .filter(text -> !text.isEmpty())
                    .collect(Collectors.joining(", "))).append("]");
        }

        sender.sendMessage("[ERR] Poprawne uzycie: " + usage);
    }
}
