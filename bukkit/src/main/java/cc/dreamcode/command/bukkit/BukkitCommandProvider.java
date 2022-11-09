package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandException;
import cc.dreamcode.command.DreamCommandProvider;
import eu.okaeri.injector.Injector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class BukkitCommandProvider implements DreamCommandProvider<BukkitCommand> {

    private final Plugin plugin;
    private final Injector injector;

    public static BukkitCommandProvider create(@NonNull Plugin plugin, @NonNull Injector injector) {
        return new BukkitCommandProvider(plugin, injector);
    }

    @Override
    public void addCommand(@NonNull Class<BukkitCommand> bukkitCommandClass) {
        this.addCommand(this.injector.createInstance(bukkitCommandClass));
    }

    @Override
    public void addCommand(@NonNull BukkitCommand command) {
        command.setPlugin(this.plugin);
        command.setInjector(this.injector);

        SimpleCommandMap simpleCommandMap = BukkitCommandReflection.getSimpleCommandMap(this.plugin.getServer());

        if (simpleCommandMap == null) {
            throw new CommandException("SimpleCommandMap not found by reflection.");
        }

        simpleCommandMap.register(this.plugin.getDescription().getName(), command);
    }
}
