package cc.dreamcode.command.bungee.suggestion.supplier;

import cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AllPlayerSupplier implements SuggestionSupplier {

    private final Plugin plugin;

    @Override
    public List<String> supply() {
        return this.plugin.getProxy().getPlayers()
                .stream()
                .map(ProxiedPlayer::getName)
                .collect(Collectors.toList());
    }
}
