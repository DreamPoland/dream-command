package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.resolver.DefaultTransformers;
import cc.dreamcode.command.resolver.ObjectResolverCache;
import cc.dreamcode.command.resolver.ObjectResolverService;
import cc.dreamcode.command.resolver.ObjectTransformer;
import lombok.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandProviderImpl implements CommandProvider {

    private final ObjectResolverCache objectResolverCache;
    private final ObjectResolverService objectResolverService;

    private final Map<String, CommandMeta> commandMap = new HashMap<>();

    public CommandProviderImpl(boolean registerDefaults) {
        this.objectResolverCache = new ObjectResolverCache();
        this.objectResolverService = new ObjectResolverService(this.objectResolverCache);

        if (registerDefaults) {
            this.registerExtension(new DefaultTransformers());
        }
    }

    @Override
    public CommandProviderImpl call(@NonNull String input) {

        final CommandParser commandParser = new CommandParser(input);

        this.commandMap.entrySet()
                .stream()
                .filter(entry -> commandParser.getInput().equalsIgnoreCase(entry.getKey()))
                .map(Map.Entry::getValue)
                .findAny()
                .ifPresent(commandMeta -> {

                    System.out.println(commandMeta.toString());

                });

        return this;
    }

    @Override
    public CommandProviderImpl register(@NonNull CommandExecutor commandExecutor) {

        final Command command = commandExecutor.getClass().getAnnotation(Command.class);
        if (command == null) {
            throw new RuntimeException("Cannot find @Command annotation in class " + commandExecutor.getClass().getSimpleName());
        }

        final CommandContext commandContext = new CommandContext(command);
        final CommandMeta commandMeta = new CommandMeta(commandContext, commandExecutor);

        this.commandMap.put(commandContext.getName(), commandMeta);
        Arrays.stream(commandContext.getAliases()).forEach(label ->
                this.commandMap.put(label, commandMeta));

        return this;
    }

    @Override
    public CommandProviderImpl registerExtension(@NonNull CommandExtension commandExtension) {
        commandExtension.register(this);
        return this;
    }

    @Override
    public CommandProviderImpl registerTransformer(@NonNull ObjectTransformer<?> objectTransformer) {
        this.objectResolverCache.add(objectTransformer);
        return this;
    }

    @Override
    public CommandProviderImpl unregisterTransformer(@NonNull Class<?> classTransformer) {
        this.objectResolverCache.remove(classTransformer);
        return this;
    }
}
