package cc.dreamcode.command.bungee;

import cc.dreamcode.command.DreamCommandImpl;
import cc.dreamcode.command.bungee.extension.BungeeExtensionRegistry;
import cc.dreamcode.command.extension.DefaultExtensionRegistry;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class BungeeCommand extends DreamCommandImpl {

    private final BungeeCommandRegistry commandRegistry;

    public BungeeCommand(@NonNull Plugin plugin) {
        this.commandRegistry = new BungeeCommandRegistry(plugin);
        this.getExtensions().registerExtension(new DefaultExtensionRegistry());
        this.getExtensions().registerExtension(new BungeeExtensionRegistry());
    }

    public static BungeeCommand create(@NonNull Plugin plugin) {
        return new BungeeCommand(plugin);
    }
}
