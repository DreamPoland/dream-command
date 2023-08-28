package cc.dreamcode.commandtest;

import cc.dreamcode.command.DreamCommandContext;
import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.DreamCommandRegistry;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.exception.CommandException;
import cc.dreamcode.command.exception.NoSuchCommandException;
import cc.dreamcode.command.path.CommandPath;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class TestCommandRegistry implements DreamCommandRegistry {

    private final Map<DreamCommandContext, DreamCommandExecutor> commandMap = new HashMap<>();

    @Override
    public void registerCommand(@NonNull DreamCommandExecutor executor) {
        final Command command = executor.getClass().getAnnotation(Command.class);
        if (command == null) {
            throw new CommandException("Cannot resolve command annotation with context. If you don't want to use annotations, you can also log commands via command registry class.");
        }

        final DreamCommandContext context = DreamCommandContext.of(command);
        this.registerCommand(context, executor);
    }

    @Override
    public void registerCommand(@NonNull DreamCommandContext context, @NonNull DreamCommandExecutor executor) {
        this.commandMap.put(context, executor);
    }

    @Override
    public void disposeCommand(@NonNull DreamCommandContext context) {
        this.commandMap.remove(context);
    }

    @Override
    public void disposeAll() {
        this.commandMap.clear();
    }

    public DreamCommandExecutor getCommand(@NonNull CommandPath commandPath) {
        return this.commandMap.entrySet()
                .stream()
                .filter(entry -> commandPath.isValid(entry.getKey()))
                .map(Map.Entry::getValue)
                .findAny()
                .orElseThrow(() -> new NoSuchCommandException(commandPath));
    }
}
