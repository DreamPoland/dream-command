package cc.dreamcode.command.bungee.extension;

import cc.dreamcode.command.bungee.extension.type.ProxiedPlayerExtension;
import cc.dreamcode.command.extension.ExtensionManager;
import cc.dreamcode.command.extension.ExtensionRegistry;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeExtensionRegistry implements ExtensionRegistry {
    @Override
    public void register(@NonNull ExtensionManager extensionManager) {
        extensionManager.registerExtension(new ProxiedPlayerExtension(), ProxiedPlayer.class);
    }
}
