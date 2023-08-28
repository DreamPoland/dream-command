package cc.dreamcode.command;

import cc.dreamcode.command.extension.ExtensionManager;
import lombok.Getter;

@Getter
public abstract class DreamCommandImpl implements DreamCommand {

    private final ExtensionManager extensions;

    public DreamCommandImpl() {
        this.extensions = new ExtensionManager();
    }
}
