package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandEntry;
import cc.dreamcode.command.CommandMeta;
import cc.dreamcode.command.CommandRegistry;
import lombok.NonNull;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

public class BukkitCommandRegistry implements CommandRegistry {

    private final Plugin plugin;
    private final BukkitCommandProvider bukkitCommandProvider;
    private final SimpleCommandMap bukkitCommandMap;

    public BukkitCommandRegistry(@NonNull Plugin plugin, @NonNull BukkitCommandProvider bukkitCommandProvider) {
        this.plugin = plugin;
        this.bukkitCommandProvider = bukkitCommandProvider;

        final SimpleCommandMap simpleCommandMap = BukkitCommandReflection.getSimpleCommandMap(plugin.getServer());
        if (simpleCommandMap == null) {
            throw new RuntimeException("SimpleCommandMap not found");
        }

        this.bukkitCommandMap = simpleCommandMap;
    }

    @Override
    public void register(@NonNull CommandEntry commandEntry, @NonNull CommandMeta commandMeta) {
        final BukkitCommandWrapper bukkitCommandWrapper = new BukkitCommandWrapper(this.plugin, commandEntry, this.bukkitCommandProvider);
        this.bukkitCommandMap.register(commandEntry.getName(), this.plugin.getName(), bukkitCommandWrapper);
    }

    @Override
    public void unregister(@NonNull CommandEntry commandEntry) {
        this.bukkitCommandMap.getCommand(commandEntry.getName()).unregister(this.bukkitCommandMap);
    }
}
