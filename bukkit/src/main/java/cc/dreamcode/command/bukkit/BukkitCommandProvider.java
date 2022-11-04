package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandException;
import cc.dreamcode.command.DreamCommandProvider;
import eu.okaeri.injector.Injector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class BukkitCommandProvider implements DreamCommandProvider<BukkitCommandHandler> {

    private final Plugin plugin;
    private final Injector injector;

    public static BukkitCommandProvider create(@NonNull Plugin plugin, @NonNull Injector injector) {
        return new BukkitCommandProvider(plugin, injector);
    }

    @Override
    public void addCommand(@NonNull Class<BukkitCommandHandler> bukkitCommandPlatformClass) {
        this.addCommand(this.injector.createInstance(bukkitCommandPlatformClass));
    }

    @Override
    public void addCommand(@NonNull BukkitCommandHandler commandHandler) {
        commandHandler.setPlugin(this.plugin);
        commandHandler.setInjector(this.injector);

        SimpleCommandMap simpleCommandMap = BukkitCommandReflection.getSimpleCommandMap(this.plugin.getServer());

        if (simpleCommandMap == null) {
            throw new CommandException("SimpleCommandMap not found by reflection.");
        }

        simpleCommandMap.register(this.plugin.getDescription().getName(), commandHandler);
    }
}
