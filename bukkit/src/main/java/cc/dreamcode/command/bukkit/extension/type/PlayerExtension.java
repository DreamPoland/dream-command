package cc.dreamcode.command.bukkit.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerExtension implements ExtensionResolver<Player> {
    @Override
    public @NonNull Player resolveArgument(@NonNull String input) throws IllegalArgumentException {
        final Optional<Player> player = Optional.ofNullable(Bukkit.getPlayerExact(input));
        if (!player.isPresent()) {
            throw new IllegalArgumentException("Player " + input + " not found");
        }

        return player.get();
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull String input) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .filter(name -> name.startsWith(input))
                .collect(Collectors.toList());
    }
}
