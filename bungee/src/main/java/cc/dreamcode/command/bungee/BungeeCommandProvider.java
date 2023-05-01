package cc.dreamcode.command.bungee;

import cc.dreamcode.command.DreamCommandProvider;
import eu.okaeri.injector.Injector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Plugin;

@RequiredArgsConstructor
public class BungeeCommandProvider implements DreamCommandProvider<BungeeCommand> {

    private final Plugin plugin;
    private final Injector injector;

    @Setter private String requiredPermissionMessage;
    @Setter private String requiredPlayerMessage;

    public static BungeeCommandProvider create(@NonNull Plugin plugin, @NonNull Injector injector) {
        return new BungeeCommandProvider(plugin, injector);
    }

    @Override
    public void addCommand(@NonNull Class<BungeeCommand> bungeeCommandClass) {
        this.addCommand(this.injector.createInstance(bungeeCommandClass));
    }

    @Override
    public void addCommand(@NonNull BungeeCommand bungeeCommand) {
        bungeeCommand.setInjector(this.injector);

        if (this.requiredPermissionMessage != null) {
            bungeeCommand.setRequiredPermissionMessage(this.requiredPermissionMessage);
        }

        if (this.requiredPlayerMessage != null) {
            bungeeCommand.setRequiredPlayerMessage(this.requiredPlayerMessage);
        }

        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, bungeeCommand);
    }
}
