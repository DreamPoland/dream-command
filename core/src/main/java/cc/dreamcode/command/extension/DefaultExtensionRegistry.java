package cc.dreamcode.command.extension;

import cc.dreamcode.command.extension.type.BooleanExtension;
import cc.dreamcode.command.extension.type.IntegerExtension;
import cc.dreamcode.command.extension.type.StringExtension;
import lombok.NonNull;

public class DefaultExtensionRegistry implements ExtensionRegistry {
    @Override
    public void register(@NonNull ExtensionManager extensionManager) {
        extensionManager.registerExtension(new StringExtension(), String.class);
        extensionManager.registerExtension(new IntegerExtension(), Integer.class, int.class);
        extensionManager.registerExtension(new BooleanExtension(), Boolean.class, boolean.class);
    }
}
