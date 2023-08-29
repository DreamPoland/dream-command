package cc.dreamcode.command;

import cc.dreamcode.command.extension.ExtensionManager;
import cc.dreamcode.command.handler.HandlerManager;
import lombok.Getter;

@Getter
public abstract class DreamCommandImpl implements DreamCommand {

    private final ExtensionManager extensions;
    private final HandlerManager handlers;

    public DreamCommandImpl() {
        this.extensions = new ExtensionManager();
        this.handlers = new HandlerManager();
    }
}
