package cc.dreamcode.command.bungee;

import cc.dreamcode.command.DreamCommandProvider;
import cc.dreamcode.notice.bungee.BungeeNotice;
import eu.okaeri.injector.Injector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Plugin;

@RequiredArgsConstructor
public class BungeeCommandProvider implements DreamCommandProvider<BungeeCommand, BungeeNotice> {

    private final Plugin plugin;
    private final Injector injector;

    @Setter private BungeeNotice noPermissionMessage;
    @Setter private BungeeNotice noPlayerMessage;

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

        if (this.noPermissionMessage != null) {
            bungeeCommand.setNoPermissionMessage(this.noPermissionMessage);
        }

        if (this.noPlayerMessage != null) {
            bungeeCommand.setNoPlayerMessage(this.noPlayerMessage);
        }

        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, bungeeCommand);
    }
}
