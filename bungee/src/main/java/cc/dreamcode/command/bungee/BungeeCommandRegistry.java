package cc.dreamcode.command.bungee;

import cc.dreamcode.command.CommandEntry;
import cc.dreamcode.command.CommandMeta;
import cc.dreamcode.command.CommandRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Locale;

@RequiredArgsConstructor
public class BungeeCommandRegistry implements CommandRegistry {

    private final Plugin plugin;
    private final BungeeCommandProvider bungeeCommandProvider;

    @Override
    public void register(@NonNull CommandEntry commandEntry, @NonNull CommandMeta commandMeta) {
        final BungeeCommandWrapper bungeeCommandWrapper = new BungeeCommandWrapper(this.bungeeCommandProvider, commandEntry);
        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, bungeeCommandWrapper);
    }

    @Override
    public void unregister(@NonNull CommandEntry commandEntry) {
        this.bungeeCommandProvider.unregister(commandEntry);
        this.plugin.getProxy().getPluginManager().getCommands()
                .stream()
                .filter(entry -> entry.getKey().equals(commandEntry.getName().toLowerCase(Locale.ROOT)))
                .forEach(entry -> this.plugin.getProxy().getPluginManager().unregisterCommand(entry.getValue()));
    }
}
