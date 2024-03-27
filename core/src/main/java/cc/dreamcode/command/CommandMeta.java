package cc.dreamcode.command;

import cc.dreamcode.command.resolver.ObjectResolverService;
import cc.dreamcode.utilities.StringUtil;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class CommandMeta {

    private final CommandContext commandContext;
    private final CommandBase commandBase;
    private final List<CommandExecutor> commandExecutors;

    public CommandMeta(@NonNull CommandContext commandContext, @NonNull CommandBase commandBase) {
        this.commandContext = commandContext;
        this.commandBase = commandBase;
        this.commandExecutors = commandBase.getMethods()
                .entrySet()
                .stream()
                .map(entry -> new CommandExecutor(this, entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());
    }

    public Optional<CommandExecutor> findExecutor(@NonNull ObjectResolverService resolverService, @NonNull CommandInput commandInput) {

        final String arguments = StringUtil.join(commandInput.getArguments(), " ");
        return this.commandExecutors.stream()
                .filter(commandExecutor -> arguments.startsWith(commandExecutor.getPattern()))
                .filter(commandExecutor -> {

                    final String scaledParams = arguments.replace(commandExecutor.getPattern() + " ", "");
                    final String[] params = scaledParams.isEmpty() ? new String[0] : scaledParams.split(" ");

                    if (params.length != commandExecutor.getParamArgs().size()) {
                        System.out.println(params.length + " " + commandExecutor.getParamArgs().size());
                        return false;
                    }

                    for (int index = 0; index < params.length; index++) {

                        final String input = params[index];
                        final Class<?> paramType = commandExecutor.getParamArgs().get(index);

                        // check transformers
                        if (!resolverService.support(paramType, input)) {
                            return false;
                        }
                    }

                    return true;
                })
                .findAny();
    }
}
