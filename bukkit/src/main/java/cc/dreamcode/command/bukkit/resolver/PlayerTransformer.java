package cc.dreamcode.command.bukkit.resolver;

import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@RequiredArgsConstructor
public class PlayerTransformer implements ObjectTransformer<Player> {

    private final Plugin plugin;

    @Override
    public Class<?> getGeneric() {
        return Player.class;
    }

    @Override
    public Optional<Player> transform(@NonNull Class<?> type, @NonNull String input) {
        return Optional.ofNullable(this.plugin.getServer().getPlayerExact(input));
    }
}
