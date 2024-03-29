package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandProviderImpl;
import cc.dreamcode.command.bukkit.bind.BukkitSenderBind;
import cc.dreamcode.command.bukkit.resolver.PlayerTransformer;
import cc.dreamcode.command.bukkit.resolver.WorldTransformer;
import cc.dreamcode.command.bukkit.suggestion.supplier.AllPlayerSupplier;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

public class BukkitCommandProvider extends CommandProviderImpl {

    public BukkitCommandProvider(@NonNull Plugin plugin) {
        super(true);

        this.setCommandRegistry(new BukkitCommandRegistry(plugin, this));

        this.registerBind(new BukkitSenderBind());

        this.registerTransformer(new PlayerTransformer(plugin));
        this.registerTransformer(new WorldTransformer(plugin));

        this.registerSuggestion("@allplayer", new AllPlayerSupplier(plugin));
        this.registerSuggestion("@allplayers", new AllPlayerSupplier(plugin));
    }

    public static BukkitCommandProvider create(@NonNull Plugin plugin) {
        return new BukkitCommandProvider(plugin);
    }
}
