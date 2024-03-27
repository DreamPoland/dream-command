package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.bind.BindCache;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.bind.BindService;
import cc.dreamcode.command.resolver.DefaultTransformers;
import cc.dreamcode.command.resolver.ResolverCache;
import cc.dreamcode.command.resolver.ResolverService;
import cc.dreamcode.command.resolver.ObjectTransformer;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandProviderImpl implements CommandProvider {

    private final BindCache bindCache;
    private final BindService bindService;
    private final ResolverCache resolverCache;
    private final ResolverService resolverService;

    private final Map<String, CommandMeta> commandMap = new HashMap<>();

    public CommandProviderImpl(boolean registerDefaults) {
        this.bindCache = new BindCache();
        this.bindService = new BindService(this.bindCache);
        this.resolverCache = new ResolverCache();
        this.resolverService = new ResolverService(this.resolverCache);

        if (registerDefaults) {
            this.registerExtension(new DefaultTransformers());
        }
    }

    @Override
    public CommandProviderImpl call(@NonNull CommandSender<?> commandSender, @NonNull String input) {

        final CommandInput commandInput = new CommandInput(input);

        final Optional<CommandExecutor> optionalCommandExecutor = this.commandMap.entrySet()
                .stream()
                .filter(entry -> commandInput.getLabel().equalsIgnoreCase(entry.getKey()))
                .map(Map.Entry::getValue)
                .findAny()
                .map(commandMeta -> commandMeta.findExecutor(this.resolverService, commandInput))
                .filter(Optional::isPresent)
                .map(Optional::get);

        if (!optionalCommandExecutor.isPresent()) {
            throw new RuntimeException("Cannot find any method with input: " + Arrays.toString(commandInput.getParams()));
        }

        final CommandExecutor commandExecutor = optionalCommandExecutor.get();
        try {
            commandExecutor.invoke(this.resolverService, this.bindService, commandInput, commandSender);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    @Override
    public CommandProviderImpl register(@NonNull CommandBase commandBase) {

        final Command command = commandBase.getClass().getAnnotation(Command.class);
        if (command == null) {
            throw new RuntimeException("Cannot find @Command annotation in class " + commandBase.getClass().getSimpleName());
        }

        final CommandContext commandContext = new CommandContext(command);
        final CommandMeta commandMeta = new CommandMeta(commandContext, commandBase);

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
        this.resolverCache.add(objectTransformer);
        return this;
    }

    @Override
    public CommandProviderImpl unregisterTransformer(@NonNull Class<?> classTransformer) {
        this.resolverCache.remove(classTransformer);
        return this;
    }

    @Override
    public CommandProviderImpl registerBind(@NonNull BindResolver<?> bindResolver) {
        this.bindCache.registerBind(bindResolver);
        return this;
    }

    @Override
    public CommandProviderImpl unregisterBind(@NonNull Class<?> bindClass) {
        this.bindCache.unregisterBind(bindClass);
        return this;
    }
}
