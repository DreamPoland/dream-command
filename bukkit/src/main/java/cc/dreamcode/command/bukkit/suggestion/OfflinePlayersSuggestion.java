package cc.dreamcode.command.bukkit.suggestion;

import cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OfflinePlayersSuggestion implements SuggestionSupplier {

    private final Plugin plugin;

    @Override
    public List<String> supply(@NonNull Class<?> paramType) {
        return Arrays.stream(this.plugin.getServer().getOfflinePlayers())
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
