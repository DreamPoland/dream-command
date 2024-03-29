package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.resolver.ResolverService;
import cc.dreamcode.utilities.StringUtil;
import lombok.Data;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
public class CommandMeta {

    private final CommandContext commandContext;
    private final CommandBase commandBase;
    private final List<CommandExecutor> commandExecutors;

    public CommandMeta(@NonNull CommandContext commandContext, @NonNull CommandBase commandBase) {
        this.commandContext = commandContext;
        this.commandBase = commandBase;
        this.commandExecutors = commandBase.getExecutors(this);
    }

    public Optional<CommandExecutor> findExecutor(@NonNull ResolverService resolverService, @NonNull CommandInput commandInput) {

        final String[] splitArguments = commandInput.getArguments();
        final String arguments = StringUtil.join(splitArguments, " ");
        return this.commandExecutors
                .stream()
                .filter(commandExecutor -> {

                    final int pathLength = commandExecutor.getPath().isEmpty() ? 0 : commandExecutor.getPath().split(" ").length;
                    if (splitArguments.length < pathLength) {
                        return false;
                    }

                    if (commandExecutor.getParamMultiArgs().isEmpty() &&
                            splitArguments.length != pathLength + commandExecutor.getParamArgs().size()) {
                        return false;
                    }

                    final String argumentEntry = StringUtil.join(splitArguments, " ", 0, pathLength);
                    return commandExecutor.getPath().equalsIgnoreCase(argumentEntry);
                })
                .sorted((o1, o2) -> {

                    final int firstPathLength = o1.getPath().isEmpty() ? 0 : o1.getPath().split(" ").length + 1;
                    final int secondPathLength = o2.getPath().isEmpty() ? 0 : o2.getPath().split(" ").length + 1;

                    return Integer.compare(secondPathLength + o2.getParamArgs().size(), firstPathLength + o1.getParamArgs().size());
                })
                .filter(commandExecutor -> {

                    final String scaledParams = arguments.replace(commandExecutor.getPath() + " ", "");
                    final String[] params = scaledParams.isEmpty() ? new String[0] : scaledParams.split(" ");

                    if (params.length < commandExecutor.getParamArgs().size()) {
                        return false;
                    }

                    final List<Class<?>> argClasses = new ArrayList<>(commandExecutor.getParamArgs().values());
                    for (int index = 0; index < commandExecutor.getParamArgs().size(); index++) {

                        final String input = params[index];
                        final Class<?> paramType = argClasses.get(index);

                        // check transformers
                        if (!resolverService.support(paramType, input)) {
                            return false;
                        }
                    }

                    if (commandExecutor.getParamMultiArgs().isEmpty()) {
                        return true;
                    }

                    // scan for @Args
                    for (Map.Entry<Integer, Class<?>> entry : commandExecutor.getParamMultiArgs().entrySet()) {
                        final int index = entry.getKey();
                        final Class<?> paramType = entry.getValue().getComponentType();

                        final Optional<Annotation> optionalAnnotation = Arrays.stream(commandExecutor.getParamAnnotations().get(index))
                                .filter(annotation -> annotation.annotationType().equals(Args.class))
                                .findAny();

                        if (!optionalAnnotation.isPresent()) {
                            throw new RuntimeException("Annotation @Args not found (critical bug)");
                        }

                        final Args args = (Args) optionalAnnotation.get();
                        final String skip = StringUtil.join(params, " ",
                                args.min() == -1 ? 0 : Math.min(args.min(), params.length),
                                args.max() == -1 ? params.length : Math.min(args.max(), params.length));

                        for (String restParam : skip.split(" ")) {
                            if (!resolverService.support(paramType, restParam)) {
                                return false;
                            }
                        }
                    }

                    return true;
                })
                .findFirst();
    }
}
