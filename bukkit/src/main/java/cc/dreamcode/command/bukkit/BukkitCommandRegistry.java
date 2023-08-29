package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.context.CommandContext;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.exception.CommandException;
import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.DreamCommandRegistry;
import lombok.NonNull;
import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class BukkitCommandRegistry implements DreamCommandRegistry {

    private final Plugin plugin;
    private final SimpleCommandMap bukkitCommandMap;

    private final Map<CommandContext, BukkitCommandExecutorWrapper> commandMap;

    public BukkitCommandRegistry(@NonNull Plugin plugin, @NonNull Server server) {
        this.plugin = plugin;

        final SimpleCommandMap simpleCommandMap = BukkitCommandReflection.getSimpleCommandMap(server);
        if (simpleCommandMap == null) {
            throw new CommandException("Cannot get a simple command map from bukkit.");
        }

        this.bukkitCommandMap = simpleCommandMap;
        this.commandMap = new HashMap<>();
    }

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
        final BukkitCommandExecutorWrapper wrapper = new BukkitCommandExecutorWrapper(this.plugin, context, executor);

        this.bukkitCommandMap.register(context.getLabel(), this.plugin.getName(), wrapper);
        this.commandMap.put(context, wrapper);
    }

    @Override
    public void disposeCommand(@NonNull CommandContext context) {
        this.bukkitCommandMap.getCommand(context.getLabel()).unregister(this.bukkitCommandMap);
        this.commandMap.remove(context);
    }

    @Override
    public void disposeAll() {
        this.commandMap.forEach((context, bukkitCommandExecutorWrapper) -> this.disposeCommand(context));
        this.commandMap.clear();
    }
}
