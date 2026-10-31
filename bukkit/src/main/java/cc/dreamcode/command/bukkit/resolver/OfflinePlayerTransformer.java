package cc.dreamcode.command.bukkit.resolver;

import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@RequiredArgsConstructor
public class OfflinePlayerTransformer implements ObjectTransformer<OfflinePlayer> {

    private final Plugin plugin;

    @Override
    public Class<?> getGeneric() {
        return OfflinePlayer.class;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Optional<OfflinePlayer> transform(@NonNull Class<?> type, @NonNull String input) {
        final Player onlinePlayer = this.plugin.getServer().getPlayerExact(input);
        if (onlinePlayer != null) {
            return Optional.of(onlinePlayer);
        }

        try {
            final OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(input);
            if (offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline()) {
                return Optional.of(offlinePlayer);
            }
        }
        catch (Exception ignored) {
        }

        return Optional.empty();
    }
}
