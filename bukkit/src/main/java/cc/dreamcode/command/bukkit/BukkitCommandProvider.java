package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.DreamCommandProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class BukkitCommandProvider implements DreamCommandProvider<BukkitCommand> {

    private final Plugin plugin;

    @Setter private String requiredPermissionMessage;
    @Setter private String requiredPlayerMessage;

    public static BukkitCommandProvider create(@NonNull Plugin plugin) {
        return new BukkitCommandProvider(plugin);
    }

    @Override
    public void addCommand(@NonNull BukkitCommand command) {
        command.setPlugin(this.plugin);

        if (this.requiredPermissionMessage != null) {
            command.setRequiredPermissionMessage(this.requiredPermissionMessage);
        }

        if (this.requiredPlayerMessage != null) {
            command.setRequiredPlayerMessage(this.requiredPlayerMessage);
        }

        SimpleCommandMap simpleCommandMap = BukkitCommandReflection.getSimpleCommandMap(this.plugin.getServer());

        if (simpleCommandMap == null) {
            throw new RuntimeException("SimpleCommandMap not found by reflection.");
        }

        simpleCommandMap.register(this.plugin.getDescription().getName(), command);
    }
}
