package cc.dreamcode.command;

import cc.dreamcode.command.bind.BindManager;
import cc.dreamcode.command.extension.ExtensionManager;
import cc.dreamcode.command.handler.HandlerManager;

public interface DreamCommand {

    DreamCommandRegistry getCommandRegistry();

    HandlerManager getHandlers();

    ExtensionManager getExtensions();

    BindManager getBinds();
}
