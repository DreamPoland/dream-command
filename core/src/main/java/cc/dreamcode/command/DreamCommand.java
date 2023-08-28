package cc.dreamcode.command;

import cc.dreamcode.command.extension.ExtensionManager;

public interface DreamCommand {

    DreamCommandRegistry getCommandRegistry();

    ExtensionManager getExtensions();
}
