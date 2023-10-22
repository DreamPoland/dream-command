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
import java.util.stream.Collectors;

public interface InvalidUsageType extends CommandHandler {

    @Override
    default HandlerType getHandlerType() {
        return HandlerType.INVALID_USAGE;
    }

    void handle(@NonNull DreamSender<?> sender, @NonNull DreamCommandExecutor executor, @NonNull List<CommandPathContext> commandPathContextList, @NonNull CommandInvokeContext commandInvokeContext);

    default String getDefaultUsage(@NonNull DreamSender<?> sender, @NonNull DreamCommandExecutor executor, @NonNull List<CommandPathContext> commandPathContextList, @NonNull CommandInvokeContext commandInvokeContext) {
        final StringBuilder stringBuilder = new StringBuilder("/" + executor.getContext().getLabel());

        final DreamCommandValidator validator = new DreamCommandValidator(commandInvokeContext);
        final List<CommandPathContext> similarOptionalPath = commandPathContextList.stream()
                .filter(optionalPath -> validator.canBeSuggestion(optionalPath, false))
                .collect(Collectors.toList());

        if (similarOptionalPath.isEmpty()) {
            final List<String> suggestion = executor.getSuggestion(sender, new CommandInvokeContext(stringBuilder.toString()));

            stringBuilder.append(" [").append(String.join(", ", suggestion)).append("]");
        }
        else if (similarOptionalPath.size() == 1) {
            final CommandPathContext commandPathContext = similarOptionalPath.get(0);

            stringBuilder.append(" ").append(commandPathContext.getPathName())
                    .append(" [").append(String.join(", ", commandPathContext.getMethodArgNames().values())).append("]");
        }
        else {
            for (int index = 0; index < commandInvokeContext.getArguments().length; index++) {
                stringBuilder.append(" ").append(commandInvokeContext.getArguments()[index]);
            }

            stringBuilder.append(" [").append(similarOptionalPath.stream()
                    .map(commandPathContext -> {
                        final String[] splitPath = commandPathContext.getPathName().split(" ");

                        // path name suggestion
                        if (splitPath.length > commandInvokeContext.getArguments().length) {
                            return splitPath[commandInvokeContext.getArguments().length];
                        }

                        // method param suggestion
                        return "<" + commandPathContext.getMethodArgNames().get(commandInvokeContext.getArguments().length - splitPath.length) + ">";
                    })
                    .collect(Collectors.joining(", "))).append("]");
        }

        return stringBuilder.toString();
    }
}
