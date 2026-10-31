package cc.dreamcode.command.bungee;

import cc.dreamcode.command.CommandPathMeta;
import cc.dreamcode.command.CommandProviderImpl;
import cc.dreamcode.command.bungee.bind.BungeeSenderBind;
import cc.dreamcode.command.bungee.bind.CommandSenderBind;
import cc.dreamcode.command.bungee.bind.ProxiedPlayerBind;
import cc.dreamcode.command.bungee.bind.player.PlayerServerBind;
import cc.dreamcode.command.bungee.resolver.ProxiedPlayerTransformer;
import cc.dreamcode.command.bungee.suggestion.AllPlayersSuggestion;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;

@Getter
public class BungeeCommandProvider extends CommandProviderImpl {

    private final Plugin plugin;

    public BungeeCommandProvider(@NonNull Plugin plugin) {
        super(true);
        this.plugin = plugin;

        this.setCommandRegistry(new BungeeCommandRegistry(plugin, this));
        this.setCommandScheduler(new BungeeCommandScheduler(plugin));

        this.registerBind(new BungeeSenderBind());
        this.registerBind(new ProxiedPlayerBind());
        this.registerBind(new CommandSenderBind());
        this.registerBind(new PlayerServerBind());

        this.registerTransformer(new ProxiedPlayerTransformer(plugin));

        final AllPlayersSuggestion allPlayersSuggestion = new AllPlayersSuggestion(plugin);
        this.registerSuggestion("@allplayers", allPlayersSuggestion);
        this.registerSuggestion("@all-players", allPlayersSuggestion);

        this.setInvalidPermissionHandler((dreamSender, permission) -> {
            ((CommandSender) dreamSender.getHandler()).sendMessage(TextComponent.fromLegacyText("§cBrak uprawnień do wykonania tej komendy! (" + permission + ")"));
        });

        this.setInvalidSenderHandler((dreamSender, requireTypes) -> {
            ((CommandSender) dreamSender.getHandler()).sendMessage(TextComponent.fromLegacyText("§cTa komenda nie może zostać wykonana przez ten typ nadawcy (" + dreamSender.getType() + ")!"));
        });

        this.setInvalidInputHandler((dreamSender, requiringClass, input) -> {
            ((CommandSender) dreamSender.getHandler()).sendMessage(TextComponent.fromLegacyText("§cPodano nieprawidłowy argument '" + input + "' (oczekiwano: " + requiringClass.getSimpleName() + ")!"));
        });

        this.setInvalidUsageHandler((dreamSender, commandMeta, commandInput) -> {
            final CommandSender sender = (CommandSender) dreamSender.getHandler();
            if (!commandMeta.isPresent()) {
                sender.sendMessage(TextComponent.fromLegacyText("§cPodano niepoprawne użycie komendy."));
                return;
            }

            final List<CommandPathMeta> filteredPaths = commandMeta.get().getFilteredCommandPaths(dreamSender);
            if (filteredPaths.isEmpty()) {
                sender.sendMessage(TextComponent.fromLegacyText("§cPodano niepoprawne użycie komendy."));
                return;
            }

            sender.sendMessage(TextComponent.fromLegacyText("§cPoprawne użycie:"));
            for (CommandPathMeta filteredPath : filteredPaths) {
                sender.sendMessage(TextComponent.fromLegacyText("§7- §e" + filteredPath.getUsage() + (filteredPath.getDescription().isEmpty() ? "" : " §8- §7" + filteredPath.getDescription())));
            }
        });
    }

    public static BungeeCommandProvider create(@NonNull Plugin plugin) {
        return new BungeeCommandProvider(plugin);
    }
}
