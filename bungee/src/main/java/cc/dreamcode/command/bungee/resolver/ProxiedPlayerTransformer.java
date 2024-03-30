package cc.dreamcode.command.bungee.resolver;

import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Optional;

@RequiredArgsConstructor
public class ProxiedPlayerTransformer implements ObjectTransformer<ProxiedPlayer> {

    private final Plugin plugin;

    @Override
    public Class<?> getGeneric() {
        return ProxiedPlayer.class;
    }

    @Override
    public Optional<ProxiedPlayer> transform(@NonNull Class<?> type, @NonNull String input) {
        return Optional.ofNullable(this.plugin.getProxy().getPlayer(input));
    }
}
