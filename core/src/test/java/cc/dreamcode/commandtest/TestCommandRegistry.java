package cc.dreamcode.commandtest;

import cc.dreamcode.command.context.CommandContext;
import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.DreamCommandRegistry;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.exception.CommandException;
import cc.dreamcode.command.exception.NoSuchCommandException;
import cc.dreamcode.command.DreamCommandValidator;
import cc.dreamcode.command.extension.ExtensionManager;
import cc.dreamcode.command.handler.HandlerManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class TestCommandRegistry implements DreamCommandRegistry {

    private final ExtensionManager extensionManager;
    private final HandlerManager handlerManager;

    private final Map<CommandContext, DreamCommandExecutor> commandMap = new HashMap<>();

    @Override
    public void registerCommand(@NonNull DreamCommandExecutor executor) {
        final Command command = executor.getClass().getAnnotation(Command.class);
        if (command == null) {
            throw new CommandException("Cannot resolve command annotation with context. If you don't want to use annotations, you can also log commands via command registry class.");
        }

        final CommandContext context = CommandContext.of(command);
        this.registerCommand(context, executor);
    }

    @Override
    public void registerCommand(@NonNull CommandContext context, @NonNull DreamCommandExecutor executor) {
        executor.setContext(context);
        executor.setExtensionManager(this.extensionManager);
        executor.setHandlerManager(this.handlerManager);

        this.commandMap.put(context, executor);
    }

    @Override
    public void disposeCommand(@NonNull CommandContext context) {
        this.commandMap.remove(context);
    }

    @Override
    public void disposeAll() {
        this.commandMap.clear();
    }

    public DreamCommandExecutor getCommand(@NonNull DreamCommandValidator dreamCommandValidator) {
        return this.commandMap.entrySet()
                .stream()
                .filter(entry -> dreamCommandValidator.isValid(entry.getKey()))
                .map(Map.Entry::getValue)
                .findAny()
                .orElseThrow(() -> new NoSuchCommandException(dreamCommandValidator));
    }
}
