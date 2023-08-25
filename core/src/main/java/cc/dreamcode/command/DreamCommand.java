package cc.dreamcode.command;

import cc.dreamcode.command.registry.DreamCommandRegistry;

public interface DreamCommand {

    DreamCommandRegistry getCommandRegistry();
}
