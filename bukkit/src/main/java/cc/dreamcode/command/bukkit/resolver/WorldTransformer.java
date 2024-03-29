package cc.dreamcode.command.bukkit.resolver;

import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@RequiredArgsConstructor
public class WorldTransformer implements ObjectTransformer<World> {

    private final Plugin plugin;

    @Override
    public Class<?> getGeneric() {
        return World.class;
    }

    @Override
    public Optional<World> transform(@NonNull Class<?> type, @NonNull String input) {
        return Optional.ofNullable(this.plugin.getServer().getWorld(input));
    }
}
