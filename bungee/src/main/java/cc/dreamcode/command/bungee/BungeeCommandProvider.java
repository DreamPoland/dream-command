package cc.dreamcode.command.bungee;

import cc.dreamcode.command.DreamCommandProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class BungeeCommandProvider implements DreamCommandProvider<BungeeCommand, CommandSender> {

    private final Plugin plugin;

    @Setter private Consumer<CommandSender> noPermissionHandler;
    @Setter private Consumer<CommandSender> notPlayerHandler;

    public static BungeeCommandProvider create(@NonNull Plugin plugin) {
        return new BungeeCommandProvider(plugin);
    }

    @Override
    public void addCommand(@NonNull BungeeCommand bungeeCommand) {
        if (this.noPermissionHandler != null) {
            bungeeCommand.setNoPermissionHandler(this.noPermissionHandler);
        }

        if (this.notPlayerHandler != null) {
            bungeeCommand.setNotPlayerHandler(this.notPlayerHandler);
        }

        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, bungeeCommand);
    }
}
