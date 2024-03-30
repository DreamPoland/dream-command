package cc.dreamcode.command.bungee;

import cc.dreamcode.command.CommandContext;
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
    public void register(@NonNull CommandContext commandContext, @NonNull CommandMeta commandMeta) {
        final BungeeCommandWrapper bungeeCommandWrapper = new BungeeCommandWrapper(commandContext, this.bungeeCommandProvider);
        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, bungeeCommandWrapper);
    }

    @Override
    public void unregister(@NonNull CommandContext commandContext) {
        this.bungeeCommandProvider.unregister(commandContext);
        this.plugin.getProxy().getPluginManager().getCommands()
                .stream()
                .filter(entry -> entry.getKey().equals(commandContext.getName().toLowerCase(Locale.ROOT)))
                .forEach(entry -> this.plugin.getProxy().getPluginManager().unregisterCommand(entry.getValue()));
    }
}
