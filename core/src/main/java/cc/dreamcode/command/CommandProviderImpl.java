package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.bind.BindCache;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.bind.BindService;
import cc.dreamcode.command.handler.InvalidInputHandler;
import cc.dreamcode.command.handler.InvalidPermissionHandler;
import cc.dreamcode.command.handler.InvalidSenderHandler;
import cc.dreamcode.command.handler.InvalidUsageHandler;
import cc.dreamcode.command.handler.exception.InvalidInputException;
import cc.dreamcode.command.handler.exception.InvalidPermissionException;
import cc.dreamcode.command.handler.exception.InvalidSenderException;
import cc.dreamcode.command.handler.exception.InvalidUsageException;
import cc.dreamcode.command.resolver.DefaultTransformers;
import cc.dreamcode.command.resolver.ResolverCache;
import cc.dreamcode.command.resolver.ResolverService;
import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import cc.dreamcode.command.suggestion.DefaultSuggestions;
import cc.dreamcode.command.suggestion.SuggestionCache;
import cc.dreamcode.command.suggestion.SuggestionService;
import cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandProviderImpl implements CommandProvider {

    private final BindCache bindCache;
    private final BindService bindService;
    private final ResolverCache resolverCache;
    private final ResolverService resolverService;
    private final SuggestionCache suggestionCache;
    private final SuggestionService suggestionService;

    private InvalidPermissionHandler invalidPermissionHandler;
    private InvalidSenderHandler invalidSenderHandler;
    private InvalidUsageHandler invalidUsageHandler;
    private InvalidInputHandler invalidInputHandler;

    private final Map<String, CommandMeta> commandMap = new HashMap<>();

    public CommandProviderImpl(boolean registerDefaults) {
        this.bindCache = new BindCache();
        this.bindService = new BindService(this.bindCache);
        this.resolverCache = new ResolverCache();
        this.resolverService = new ResolverService(this.resolverCache);
        this.suggestionCache = new SuggestionCache();
        this.suggestionService = new SuggestionService(this.suggestionCache);

        if (registerDefaults) {
            this.registerExtension(new DefaultTransformers());
            this.registerExtension(new DefaultSuggestions());
        }
    }

    @Override
    public List<String> getSuggestion(@NonNull String input) {
        final CommandInput commandInput = new CommandInput(input);
        return this.commandMap.entrySet()
                .stream()
                .filter(entry -> commandInput.getLabel().equalsIgnoreCase(entry.getKey()))
                .map(entry -> entry.getValue().getSuggestion(this.suggestionService, commandInput))
                .findAny()
                .orElse(new ArrayList<>());
    }

    @Override
    public CommandProviderImpl call(@NonNull CommandSender<?> commandSender, @NonNull String input) {
        return this.call(commandSender, new CommandInput(input));
    }

    @Override
    public CommandProviderImpl call(@NonNull CommandSender<?> commandSender, @NonNull CommandInput commandInput) {

        final Optional<CommandMeta> optionalCommandMeta = this.commandMap.entrySet()
                .stream()
                .filter(entry -> commandInput.getLabel().equalsIgnoreCase(entry.getKey()))
                .map(Map.Entry::getValue)
                .findAny();

        final Optional<CommandPathMeta> optionalCommandPathMeta = optionalCommandMeta
                .map(commandMeta -> commandMeta.findExecutor(this.resolverService, commandInput))
                .filter(Optional::isPresent)
                .map(Optional::get);

        try {
            if (!optionalCommandPathMeta.isPresent()) {

                if (optionalCommandMeta.isPresent()) {
                    final CommandMeta commandMeta = optionalCommandMeta.get();

                    if (this.invalidUsageHandler != null) {
                        this.invalidUsageHandler.handle(commandSender, Optional.of(commandMeta), commandInput);
                        return this;
                    }

                    throw new InvalidUsageException(commandMeta, commandInput, "Cannot find any path with input: " + Arrays.toString(commandInput.getParams()));
                }

                throw new InvalidUsageException(null, commandInput, "Cannot find any method with input: " + Arrays.toString(commandInput.getParams()));
            }

            final CommandPathMeta commandPathMeta = optionalCommandPathMeta.get();

            final CommandExecutor commandExecutor = commandPathMeta.getCommandExecutor();
            commandExecutor.invoke(this.resolverService, this.bindService, commandInput, commandSender);
        }
        catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvalidInputException e) {
            if (this.invalidInputHandler != null) {
                this.invalidInputHandler.handle(commandSender, e.getRequiringClass(), e.getInput());
                return this;
            }

            throw e;
        }
        catch (InvalidPermissionException e) {
            if (this.invalidPermissionHandler != null) {
                this.invalidPermissionHandler.handle(commandSender, e.getPermission());
                return this;
            }

            throw e;
        }
        catch (InvalidSenderException e) {
            if (this.invalidSenderHandler != null) {
                this.invalidSenderHandler.handle(commandSender, e.getRequireType());
                return this;
            }

            throw e;
        }
        catch (InvalidUsageException e) {
            if (this.invalidUsageHandler != null) {
                this.invalidUsageHandler.handle(commandSender, Optional.ofNullable(e.getCommandMeta()), e.getCommandInput());
                return this;
            }

            throw e;
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
    public CommandProviderImpl registerTransformer(@NonNull ArrayTransformer<?> arrayTransformer) {
        this.resolverCache.add(arrayTransformer);
        return this;
    }

    @Override
    public CommandProviderImpl unregisterTransformer(@NonNull Class<?> classTransformer, boolean array) {
        this.resolverCache.remove(classTransformer);

        if (array) {
            this.resolverCache.removeArray(classTransformer);
        }

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

    @Override
    public CommandProviderImpl registerSuggestion(@NonNull String key, @NonNull SuggestionSupplier suggestionSupplier) {
        this.suggestionCache.addSuggestion(key, suggestionSupplier);
        return this;
    }

    @Override
    public CommandProviderImpl unregisterSuggestion(@NonNull String key) {
        this.suggestionCache.removeSuggestion(key);
        return this;
    }

    @Override
    public CommandProviderImpl registerSuggestionFilter(@NonNull String key, @NonNull SuggestionFilter suggestionFilter) {
        this.suggestionCache.addSuggestionFilter(key, suggestionFilter);
        return this;
    }

    @Override
    public CommandProviderImpl unregisterSuggestionFilter(@NonNull String key) {
        this.suggestionCache.removeSuggestionFilter(key);
        return this;
    }

    @Override
    public InvalidPermissionHandler getInvalidPermissionHandler() {
        return this.invalidPermissionHandler;
    }

    @Override
    public CommandProviderImpl setInvalidPermissionHandler(@NonNull InvalidPermissionHandler invalidPermissionHandler) {
        this.invalidPermissionHandler = invalidPermissionHandler;
        return this;
    }

    @Override
    public InvalidSenderHandler getInvalidSenderHandler() {
        return this.invalidSenderHandler;
    }

    @Override
    public CommandProviderImpl setInvalidSenderHandler(@NonNull InvalidSenderHandler invalidSenderHandler) {
        this.invalidSenderHandler = invalidSenderHandler;
        return this;
    }

    @Override
    public InvalidUsageHandler getInvalidUsageHandler() {
        return this.invalidUsageHandler;
    }

    @Override
    public CommandProviderImpl setInvalidUsageHandler(@NonNull InvalidUsageHandler invalidUsageHandler) {
        this.invalidUsageHandler = invalidUsageHandler;
        return this;
    }

    @Override
    public InvalidInputHandler getInvalidInputHandler() {
        return this.invalidInputHandler;
    }

    @Override
    public CommandProviderImpl setInvalidInputHandler(@NonNull InvalidInputHandler invalidInputHandler) {
        this.invalidInputHandler = invalidInputHandler;
        return this;
    }
}
