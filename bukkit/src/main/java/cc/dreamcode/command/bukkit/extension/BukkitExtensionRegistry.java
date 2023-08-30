package cc.dreamcode.command.bukkit.extension;

import cc.dreamcode.command.bukkit.extension.type.PlayerExtension;
import cc.dreamcode.command.extension.ExtensionManager;
import cc.dreamcode.command.extension.ExtensionRegistry;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class BukkitExtensionRegistry implements ExtensionRegistry {
    @Override
    public void register(@NonNull ExtensionManager extensionManager) {
        extensionManager.registerExtension(new PlayerExtension(), Player.class);
    }
}
