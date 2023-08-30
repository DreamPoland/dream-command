package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.DreamCommandImpl;
import cc.dreamcode.command.bukkit.extension.BukkitExtensionRegistry;
import cc.dreamcode.command.extension.DefaultExtensionRegistry;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

@Getter
public class BukkitCommand extends DreamCommandImpl {

    private final BukkitCommandRegistry commandRegistry;

    public BukkitCommand(@NonNull Plugin plugin) {
        this.commandRegistry = new BukkitCommandRegistry(plugin, plugin.getServer());
        this.getExtensions().registerExtension(new DefaultExtensionRegistry());
        this.getExtensions().registerExtension(new BukkitExtensionRegistry());
    }
}
