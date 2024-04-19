package cc.dreamcode.command.bungee;

import cc.dreamcode.command.CommandProviderImpl;
import cc.dreamcode.command.bungee.bind.BungeeSenderBind;
import cc.dreamcode.command.bungee.bind.CommandSenderBind;
import cc.dreamcode.command.bungee.bind.ProxiedPlayerBind;
import cc.dreamcode.command.bungee.bind.player.PlayerServerBind;
import cc.dreamcode.command.bungee.resolver.ProxiedPlayerTransformer;
import cc.dreamcode.command.bungee.suggestion.supplier.AllPlayerSupplier;
import lombok.NonNull;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeCommandProvider extends CommandProviderImpl {

    public BungeeCommandProvider(@NonNull Plugin plugin) {
        super(true);

        this.setCommandRegistry(new BungeeCommandRegistry(plugin, this));
        this.setCommandScheduler(new BungeeCommandScheduler(plugin));

        this.registerBind(new BungeeSenderBind());
        this.registerBind(new ProxiedPlayerBind());
        this.registerBind(new CommandSenderBind());
        this.registerBind(new PlayerServerBind());

        this.registerTransformer(new ProxiedPlayerTransformer(plugin));

        this.registerSuggestion("@allplayer", new AllPlayerSupplier(plugin));
        this.registerSuggestion("@allplayers", new AllPlayerSupplier(plugin));
    }

    public static BungeeCommandProvider create(@NonNull Plugin plugin) {
        return new BungeeCommandProvider(plugin);
    }
}
