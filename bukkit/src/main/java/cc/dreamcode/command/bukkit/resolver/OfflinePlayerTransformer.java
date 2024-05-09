package cc.dreamcode.command.bukkit.resolver;

import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class OfflinePlayerTransformer implements ObjectTransformer<OfflinePlayer> {

    private final Plugin plugin;

    @Override
    public Class<?> getGeneric() {
        return OfflinePlayer.class;
    }

    @Override
    public Optional<OfflinePlayer> transform(@NonNull Class<?> type, @NonNull String input) {
        return Arrays.stream(this.plugin.getServer().getOfflinePlayers())
                .filter(offlinePlayer -> offlinePlayer.getName() != null)
                .filter(offlinePlayer -> offlinePlayer.getName().equalsIgnoreCase(input))
                .findAny();
    }
}
