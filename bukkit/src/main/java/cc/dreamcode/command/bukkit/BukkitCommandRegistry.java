package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandEntry;
import cc.dreamcode.command.CommandMeta;
import cc.dreamcode.command.CommandRegistry;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.util.Map;

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
        final Command command = this.bukkitCommandMap.getCommand(commandEntry.getName());
        if (command != null) {
            command.unregister(this.bukkitCommandMap);
        }

        final Map<String, Command> knownCommands = BukkitCommandReflection.getKnownCommands(this.bukkitCommandMap);
        if (knownCommands != null) {
            knownCommands.remove(commandEntry.getName().toLowerCase());
            knownCommands.remove(this.plugin.getName().toLowerCase() + ":" + commandEntry.getName().toLowerCase());
            for (String alias : commandEntry.getAliases()) {
                knownCommands.remove(alias.toLowerCase());
                knownCommands.remove(this.plugin.getName().toLowerCase() + ":" + alias.toLowerCase());
            }
        }
    }
}
