package cc.dreamcode.command.bungee;

import cc.dreamcode.command.DreamCommandProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Plugin;

@RequiredArgsConstructor
public class BungeeCommandProvider implements DreamCommandProvider<BungeeCommand> {

    private final Plugin plugin;

    @Setter private String requiredPermissionMessage;
    @Setter private String requiredPlayerMessage;

    public static BungeeCommandProvider create(@NonNull Plugin plugin) {
        return new BungeeCommandProvider(plugin);
    }

    @Override
    public void addCommand(@NonNull BungeeCommand bungeeCommand) {
        if (this.requiredPermissionMessage != null) {
            bungeeCommand.setRequiredPermissionMessage(this.requiredPermissionMessage);
        }

        if (this.requiredPlayerMessage != null) {
            bungeeCommand.setRequiredPlayerMessage(this.requiredPlayerMessage);
        }

        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, bungeeCommand);
    }
}
