package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.Async;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.command.annotation.Sender;
import cc.dreamcode.command.handler.exception.InvalidInputException;
import cc.dreamcode.command.resolver.ResolverService;
import cc.dreamcode.command.suggestion.SuggestionService;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.Data;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class CommandMeta {

    private final CommandContext commandContext;
    private final CommandBase commandBase;

    private final boolean async;
    private final String[] basePermissions;
    private final DreamSender.Type[] baseSenderTypes;

    private final List<CommandPathMeta> commandPaths;

    private final SuggestionService suggestionService;
    private final ResolverService resolverService;

    public CommandMeta(@NonNull SuggestionService suggestionService, @NonNull ResolverService resolverService, @NonNull CommandContext commandContext, @NonNull CommandBase commandBase) {
        this.suggestionService = suggestionService;
        this.resolverService = resolverService;
        this.commandContext = commandContext;
        this.commandBase = commandBase;

        this.async = commandBase.getClass().getAnnotation(Async.class) != null;

        final Permission[] permissionsArray = commandBase.getClass().getAnnotationsByType(Permission.class);
        this.basePermissions = Arrays.stream(permissionsArray)
                .map(Permission::value)
                .toArray(String[]::new);

        final Sender[] sendersArray = commandBase.getClass().getAnnotationsByType(Sender.class);
        this.baseSenderTypes = Arrays.stream(sendersArray)
                .map(Sender::value)
                .toArray(DreamSender.Type[]::new);

        this.commandPaths = commandBase.getCommandPaths(this);
    }

    public List<CommandPathMeta> getFilteredCommandPaths(@NonNull DreamSender<?> sender) {
        return this.commandPaths
                .stream()
                .filter(commandPathMeta -> {
                    final List<DreamSender.Type> senderTypes = commandPathMeta.getSendersType();
                    return senderTypes.isEmpty() || senderTypes.contains(sender.getType());
                })
                .filter(commandPathMeta -> {
                    for (String permission : commandPathMeta.getPermissions()) {
                        if (!sender.hasPermission(permission)) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }

    public List<String> getSuggestion(@NonNull DreamSender<?> sender, @NonNull CommandInput commandInput) {

        final ListBuilder<String> listBuilder = new ListBuilder<>();

        for (CommandPathMeta commandPath : this.getFilteredCommandPaths(sender)) {
            listBuilder.addAll(commandPath.getSuggestion(this.suggestionService, commandInput));
        }

        return listBuilder.build();
    }

    public Stream<CommandPathMeta> findExecutor(@NonNull CommandInput commandInput, boolean throwInvalidInput) {
        return this.commandPaths
                .stream()
                .filter(commandPathMeta -> {

                    final int pathLength = commandPathMeta.getPath().isEmpty() ? 0 : commandPathMeta.getPath().split(" ").length;
                    if (commandInput.getArguments().length < pathLength) {
                        return false;
                    }

                    if (commandPathMeta.getParamMultiArgs().isEmpty() &&
                            commandInput.getArguments().length > pathLength + commandPathMeta.getParamArgs().size() + commandPathMeta.getParamOptionalArgs().size()) {
                        return false;
                    }

                    final String argumentEntry = StringUtil.join(commandInput.getArguments(), " ", 0, pathLength);
                    return commandPathMeta.getPath().equalsIgnoreCase(argumentEntry);
                })
                .sorted((o1, o2) -> {

                    final int firstPathLength = o1.getPath().isEmpty() ? 0 : o1.getPath().split(" ").length + 1;
                    final int secondPathLength = o2.getPath().isEmpty() ? 0 : o2.getPath().split(" ").length + 1;

                    return Integer.compare(secondPathLength + o2.getParamArgs().size(), firstPathLength + o1.getParamArgs().size());
                })
                .filter(commandPathMeta -> {

                    final int pathLength = commandPathMeta.getPath().isEmpty() ? 0 : commandPathMeta.getPath().split(" ").length;

                    final String[] params = new String[commandInput.getArguments().length - pathLength];
                    System.arraycopy(commandInput.getArguments(), pathLength, params, 0, params.length);

                    if (params.length < commandPathMeta.getParamArgs().size()) {
                        return false;
                    }

                    final List<Class<?>> argClasses = new ArrayList<>(commandPathMeta.getParamArgs().values());
                    for (int index = 0; index < commandPathMeta.getParamArgs().size(); index++) {

                        final String input = params[index];
                        final Class<?> paramType = argClasses.get(index);

                        // check transformers
                        if (!this.resolverService.support(paramType, input)) {
                            if (throwInvalidInput) {
                                throw new InvalidInputException(paramType, input, "Cannot resolve param " + input + " as a " + paramType.getSimpleName());
                            }

                            return false;
                        }
                    }

                    if (commandPathMeta.getParamMultiArgs().isEmpty()) {
                        return true;
                    }

                    // scan for @Args
                    for (Map.Entry<Integer, Class<?>> entry : commandPathMeta.getParamMultiArgs().entrySet()) {
                        final int index = entry.getKey();
                        final Class<?> paramType = entry.getValue().getComponentType();

                        final Optional<Annotation> optionalAnnotation = Arrays.stream(commandPathMeta.getParamAnnotations().get(index))
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
                            if (!this.resolverService.support(paramType, restParam)) {
                                if (throwInvalidInput) {
                                    throw new InvalidInputException(paramType, restParam, "Cannot resolve param " + restParam + " as a " + paramType.getSimpleName());
                                }

                                return false;
                            }
                        }
                    }

                    return true;
                });
    }
}
