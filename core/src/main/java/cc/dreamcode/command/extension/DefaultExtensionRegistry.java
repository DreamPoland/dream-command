package cc.dreamcode.command.extension;

import cc.dreamcode.command.extension.type.StringExtension;
import lombok.NonNull;

public class DefaultExtensionRegistry implements ExtensionRegistry {
    @Override
    public void register(@NonNull ExtensionManager extensionManager) {
        extensionManager.registerExtension(String.class, new StringExtension());
    }
}
