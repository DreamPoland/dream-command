package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandProviderImpl;
import cc.dreamcode.command.bukkit.bind.BukkitSenderBind;
import cc.dreamcode.command.bukkit.bind.CommandSenderBind;
import cc.dreamcode.command.bukkit.bind.ConsoleCommandSenderBind;
import cc.dreamcode.command.bukkit.bind.PlayerBind;
import cc.dreamcode.command.bukkit.bind.ServerBind;
import cc.dreamcode.command.bukkit.bind.player.PlayerGamemodeBind;
import cc.dreamcode.command.bukkit.bind.player.PlayerInventoryBind;
import cc.dreamcode.command.bukkit.bind.player.PlayerLocationBind;
import cc.dreamcode.command.bukkit.bind.player.PlayerWorldBind;
import cc.dreamcode.command.bukkit.resolver.PlayerTransformer;
import cc.dreamcode.command.bukkit.resolver.WorldTransformer;
import cc.dreamcode.command.bukkit.suggestion.supplier.AllPlayerSupplier;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

public class BukkitCommandProvider extends CommandProviderImpl {

    public BukkitCommandProvider(@NonNull Plugin plugin) {
        super(true);

        this.setCommandRegistry(new BukkitCommandRegistry(plugin, this));
        this.setCommandScheduler(new BukkitCommandScheduler(plugin));

        this.registerBind(new BukkitSenderBind());
        this.registerBind(new PlayerBind());
        this.registerBind(new CommandSenderBind());
        this.registerBind(new ConsoleCommandSenderBind());
        this.registerBind(new ServerBind());

        this.registerBind(new PlayerGamemodeBind());
        this.registerBind(new PlayerInventoryBind());
        this.registerBind(new PlayerLocationBind());
        this.registerBind(new PlayerWorldBind());

        this.registerTransformer(new PlayerTransformer(plugin));
        this.registerTransformer(new WorldTransformer(plugin));

        this.registerSuggestion("@allplayer", new AllPlayerSupplier(plugin));
        this.registerSuggestion("@allplayers", new AllPlayerSupplier(plugin));
    }

    public static BukkitCommandProvider create(@NonNull Plugin plugin) {
        return new BukkitCommandProvider(plugin);
    }
}
