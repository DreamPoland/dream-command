package cc.dreamcode.command.bungee.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProxiedPlayerExtension implements ExtensionResolver<ProxiedPlayer> {
    @Override
    public @NonNull ProxiedPlayer resolveArgument(@NonNull Class<?> argumentClass, @NonNull String input) throws IllegalArgumentException {
        final Optional<ProxiedPlayer> proxiedPlayer = Optional.ofNullable(ProxyServer.getInstance().getPlayer(input));
        if (!proxiedPlayer.isPresent()) {
            throw new IllegalArgumentException("ProxiedPlayer " + input + " not found");
        }

        return proxiedPlayer.get();
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull Class<?> argumentClass, @NonNull String input) {
        return ProxyServer.getInstance().getPlayers()
                .stream()
                .map(ProxiedPlayer::getName)
                .collect(Collectors.toList());
    }
}
