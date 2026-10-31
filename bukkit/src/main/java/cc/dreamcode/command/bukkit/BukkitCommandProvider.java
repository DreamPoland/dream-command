package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandPathMeta;
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
import cc.dreamcode.command.bukkit.resolver.OfflinePlayerTransformer;
import cc.dreamcode.command.bukkit.resolver.PlayerTransformer;
import cc.dreamcode.command.bukkit.resolver.WorldTransformer;
import cc.dreamcode.command.bukkit.suggestion.AllPlayersSuggestion;
import cc.dreamcode.command.bukkit.suggestion.OfflinePlayersSuggestion;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;

@Getter
public class BukkitCommandProvider extends CommandProviderImpl {

    private final Plugin plugin;

    public BukkitCommandProvider(@NonNull Plugin plugin) {
        super(true);
        this.plugin = plugin;

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
        this.registerTransformer(new OfflinePlayerTransformer(plugin));

        final AllPlayersSuggestion allPlayersSuggestion = new AllPlayersSuggestion(plugin);
        this.registerSuggestion("@allplayers", allPlayersSuggestion);
        this.registerSuggestion("@all-players", allPlayersSuggestion);

        final OfflinePlayersSuggestion offlinePlayersSuggestion = new OfflinePlayersSuggestion(plugin);
        this.registerSuggestion("@offlineplayers", offlinePlayersSuggestion);
        this.registerSuggestion("@offline-players", offlinePlayersSuggestion);

        this.setInvalidPermissionHandler((dreamSender, permission) -> {
            ((CommandSender) dreamSender.getHandler()).sendMessage("§cBrak uprawnień do wykonania tej komendy! (" + permission + ")");
        });

        this.setInvalidSenderHandler((dreamSender, requireTypes) -> {
            ((CommandSender) dreamSender.getHandler()).sendMessage("§cTa komenda nie może zostać wykonana przez ten typ nadawcy (" + dreamSender.getType() + ")!");
        });

        this.setInvalidInputHandler((dreamSender, requiringClass, input) -> {
            ((CommandSender) dreamSender.getHandler()).sendMessage("§cPodano nieprawidłowy argument '" + input + "' (oczekiwano: " + requiringClass.getSimpleName() + ")!");
        });

        this.setInvalidUsageHandler((dreamSender, commandMeta, commandInput) -> {
            final CommandSender sender = (CommandSender) dreamSender.getHandler();
            if (!commandMeta.isPresent()) {
                sender.sendMessage("§cPodano niepoprawne użycie komendy.");
                return;
            }

            final List<CommandPathMeta> filteredPaths = commandMeta.get().getFilteredCommandPaths(dreamSender);
            if (filteredPaths.isEmpty()) {
                sender.sendMessage("§cPodano niepoprawne użycie komendy.");
                return;
            }

            sender.sendMessage("§cPoprawne użycie:");
            for (CommandPathMeta filteredPath : filteredPaths) {
                sender.sendMessage("§7- §e" + filteredPath.getUsage() + (filteredPath.getDescription().isEmpty() ? "" : " §8- §7" + filteredPath.getDescription()));
            }
        });
    }

    public static BukkitCommandProvider create(@NonNull Plugin plugin) {
        return new BukkitCommandProvider(plugin);
    }
}
