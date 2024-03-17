package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.DreamCommandProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class BukkitCommandProvider implements DreamCommandProvider<BukkitCommand, CommandSender> {

    private final Plugin plugin;

    @Setter private Consumer<CommandSender> noPermissionHandler;
    @Setter private Consumer<CommandSender> notPlayerHandler;

    public static BukkitCommandProvider create(@NonNull Plugin plugin) {
        return new BukkitCommandProvider(plugin);
    }

    @Override
    public void addCommand(@NonNull BukkitCommand command) {
        command.setPlugin(this.plugin);

        if (this.noPermissionHandler != null) {
            command.setNoPermissionHandler(this.noPermissionHandler);
        }

        if (this.notPlayerHandler != null) {
            command.setNotPlayerHandler(this.notPlayerHandler);
        }

        SimpleCommandMap simpleCommandMap = BukkitCommandReflection.getSimpleCommandMap(this.plugin.getServer());

        if (simpleCommandMap == null) {
            throw new RuntimeException("SimpleCommandMap not found by reflection.");
        }

        simpleCommandMap.register(this.plugin.getDescription().getName(), command);
    }
}
