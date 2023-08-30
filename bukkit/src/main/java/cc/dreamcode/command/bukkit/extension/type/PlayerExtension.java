package cc.dreamcode.command.bukkit.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlayerExtension implements ExtensionResolver<Player> {
    @Override
    public @NonNull Player resolveArgument(@NonNull String input) throws IllegalArgumentException {
        final Optional<Player> player = Optional.ofNullable(Bukkit.getPlayerExact(input));
        if (!player.isPresent()) {
            throw new IllegalArgumentException("Player " + input + " not found");
        }

        return player.get();
    }
}
