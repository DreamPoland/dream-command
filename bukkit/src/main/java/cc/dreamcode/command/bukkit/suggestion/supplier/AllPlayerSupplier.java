package cc.dreamcode.command.bukkit.suggestion.supplier;

import cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AllPlayerSupplier implements SuggestionSupplier {

    private final Plugin plugin;

    @Override
    public List<String> supply() {
        return this.plugin.getServer().getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }
}
