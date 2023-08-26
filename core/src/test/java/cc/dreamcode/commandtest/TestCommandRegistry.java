package cc.dreamcode.commandtest;

import cc.dreamcode.command.DreamCommandContext;
import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.DreamCommandRegistry;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestCommandRegistry implements DreamCommandRegistry {

    private final Map<DreamCommandContext, DreamCommandExecutor> commandMap = new HashMap<>();

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

    public Optional<DreamCommandExecutor> getCommandByName(@NonNull String name) {
        return this.commandMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().getName().equals(name))
                .map(Map.Entry::getValue)
                .findAny();
    }

    public void parseCommand(@NonNull String path) {
        final String[] args = path.split(" ");
        if (args.length == 0) {
            throw new RuntimeException("Cannot parse empty command");
        }

        final String commandPath = args[0];
        this.getCommandByName(commandPath).ifPresent(executor -> executor.invokeMethod(path));
    }
}
