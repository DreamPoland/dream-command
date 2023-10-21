package cc.dreamcode.command.handler.type;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.DreamCommandValidator;
import cc.dreamcode.command.context.CommandInvokeContext;
import cc.dreamcode.command.context.CommandPathContext;
import cc.dreamcode.command.handler.CommandHandler;
import cc.dreamcode.command.handler.HandlerType;
import cc.dreamcode.command.sender.DreamSender;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface InvalidUsageType extends CommandHandler {

    @Override
    default HandlerType getHandlerType() {
        return HandlerType.INVALID_USAGE;
    }

    void handle(@NonNull DreamSender<?> sender, @NonNull DreamCommandExecutor executor, @NonNull List<CommandPathContext> commandPathContextList, @NonNull CommandInvokeContext commandInvokeContext);

    default String getDefaultUsage(@NonNull DreamCommandExecutor executor, @NonNull List<CommandPathContext> commandPathContextList, @NonNull CommandInvokeContext commandInvokeContext) {
        final StringBuilder usage = new StringBuilder("/" + executor.getContext().getLabel());

        final DreamCommandValidator validator = new DreamCommandValidator(commandInvokeContext);
        final List<CommandPathContext> similarOptionalPath = commandPathContextList.stream()
                .filter(validator::canBeSuggestion)
                .collect(Collectors.toList());

        if (similarOptionalPath.size() > 1) { // suggest some option
            usage.append(" [").append(similarOptionalPath.stream()
                    .map(commandPathContext -> {
                        final String[] pathNameRow = commandPathContext.getPathName().split(" ");
                        if (pathNameRow.length > commandInvokeContext.getArguments().length) {
                            return pathNameRow[commandInvokeContext.getArguments().length];
                        }

                        return "[" + commandPathContext.getMethodArgNames()
                                .entrySet()
                                .stream()
                                .filter(entry -> entry.getKey() == commandInvokeContext.getArguments().length - pathNameRow.length)
                                .map(Map.Entry::getValue)
                                .collect(Collectors.joining(", ")) + "]";
                    })
                    .distinct()
                    .filter(text -> !text.isEmpty())
                    .collect(Collectors.joining(", "))).append("]");
        }
        else if (similarOptionalPath.size() == 1) { // suggest last option
            final CommandPathContext similarPath = similarOptionalPath.get(0);
            if (!similarPath.getPathName().isEmpty()) {
                usage.append(" ").append(similarPath.getPathName());
            }

            if (!similarPath.getMethodArg().isEmpty() || !similarPath.getMethodArgs().isEmpty()) {
                usage.append(" (").append(String.join(", ", similarPath.getMethodArgNames().values())).append(")");
            }
        }
        else { // suggest anything
            usage.append(" [").append(commandPathContextList.stream()
                    .map(CommandPathContext::getPathName)
                    .distinct()
                    .filter(text -> !text.isEmpty())
                    .collect(Collectors.joining(", "))).append("]");
        }


        return usage.toString();
    }
}
