package cc.dreamcode.command.bukkit.suggestion;

import cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OfflinePlayersSuggestion implements SuggestionSupplier {

    private final Plugin plugin;
    private long lastFetch = 0L;
    private List<String> cachedPlayers = Collections.emptyList();

    public OfflinePlayersSuggestion(@NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public synchronized List<String> supply(@NonNull Class<?> paramType) {
        final long now = System.currentTimeMillis();
        if (now - this.lastFetch > 30_000L || this.cachedPlayers.isEmpty()) {
            this.lastFetch = now;
            this.cachedPlayers = Arrays.stream(this.plugin.getServer().getOfflinePlayers())
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>(this.cachedPlayers);
    }
}
