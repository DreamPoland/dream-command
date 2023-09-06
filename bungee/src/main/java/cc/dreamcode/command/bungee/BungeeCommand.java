package cc.dreamcode.command.bungee;

import cc.dreamcode.command.DreamCommandImpl;
import cc.dreamcode.command.bungee.bind.BungeeBindRegistry;
import cc.dreamcode.command.bungee.extension.BungeeExtensionRegistry;
import cc.dreamcode.command.extension.DefaultExtensionRegistry;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class BungeeCommand extends DreamCommandImpl {

    private final BungeeCommandRegistry commandRegistry;

    public BungeeCommand(@NonNull Plugin plugin) {
        this.commandRegistry = new BungeeCommandRegistry(plugin, this);
        this.getExtensions().registerExtension(new DefaultExtensionRegistry());
        this.getExtensions().registerExtension(new BungeeExtensionRegistry());
        this.getBinds().registerBind(new BungeeBindRegistry());
    }

    public static BungeeCommand create(@NonNull Plugin plugin) {
        return new BungeeCommand(plugin);
    }
}
