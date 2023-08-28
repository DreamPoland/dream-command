package cc.dreamcode.command.extension;

import lombok.NonNull;

public interface ExtensionRegistry {
    void register(@NonNull ExtensionManager extensionManager);
}
