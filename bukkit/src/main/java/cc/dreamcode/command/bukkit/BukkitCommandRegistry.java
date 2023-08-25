package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.DreamCommandContext;
import cc.dreamcode.command.DreamCommandException;
import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.DreamCommandRegistry;
import cc.dreamcode.command.bukkit.wrapper.BukkitCommandExecutorWrapper;
import lombok.NonNull;
import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class BukkitCommandRegistry implements DreamCommandRegistry {

    private final Plugin plugin;
    private final SimpleCommandMap bukkitCommandMap;

    private final Map<DreamCommandContext, BukkitCommandExecutorWrapper> commandMap;

    public BukkitCommandRegistry(@NonNull Plugin plugin, @NonNull Server server) {
        this.plugin = plugin;

        final SimpleCommandMap simpleCommandMap = BukkitCommandReflection.getSimpleCommandMap(server);
        if (simpleCommandMap == null) {
            throw new DreamCommandException("Cannot get a simple command map from bukkit.");
        }

        this.bukkitCommandMap = simpleCommandMap;
        this.commandMap = new HashMap<>();
    }

    @Override
    public void registerCommand(@NonNull DreamCommandContext context, @NonNull DreamCommandExecutor executor) {
        final BukkitCommandExecutorWrapper wrapper = new BukkitCommandExecutorWrapper(this.plugin, context, executor);

        this.bukkitCommandMap.register(context.getName(), this.plugin.getName(), wrapper);
        this.commandMap.put(context, wrapper);
    }

    @Override
    public void disposeCommand(@NonNull DreamCommandContext context) {
        this.bukkitCommandMap.getCommand(context.getName()).unregister(this.bukkitCommandMap);
        this.commandMap.remove(context);
    }

    @Override
    public void disposeAll() {
        this.commandMap.forEach((context, bukkitCommandExecutorWrapper) -> this.disposeCommand(context));
        this.commandMap.clear();
    }
}
