package cc.dreamcode.command;

import cc.dreamcode.command.bind.BindManager;
import cc.dreamcode.command.extension.ExtensionManager;
import cc.dreamcode.command.handler.HandlerManager;
import lombok.Getter;

@Getter
public abstract class DreamCommandImpl implements DreamCommand {

    private final ExtensionManager extensions;
    private final HandlerManager handlers;
    private final BindManager binds;

    public DreamCommandImpl() {
        this.extensions = new ExtensionManager();
        this.handlers = new HandlerManager();
        this.binds = new BindManager();
    }
}
