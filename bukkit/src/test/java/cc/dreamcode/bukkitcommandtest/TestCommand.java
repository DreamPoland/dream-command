package cc.dreamcode.bukkitcommandtest;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.annotation.Command;

@Command(label = "test", description = "Test command.", aliases = "test")
public class TestCommand implements DreamCommandExecutor {
}
