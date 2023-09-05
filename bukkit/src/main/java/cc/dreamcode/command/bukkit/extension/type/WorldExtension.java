package cc.dreamcode.command.bukkit.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WorldExtension implements ExtensionResolver<World> {
    @Override
    public @NonNull World resolveArgument(@NonNull Class<?> argumentClass, @NonNull String input) throws IllegalArgumentException {
        final Optional<World> world = Optional.ofNullable(Bukkit.getWorld(input));
        if (!world.isPresent()) {
            throw new IllegalArgumentException("World " + input + " not found");
        }

        return world.get();
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull Class<?> argumentClass, @NonNull String input) {
        return Bukkit.getWorlds()
                .stream()
                .map(World::getName)
                .collect(Collectors.toList());
    }
}
