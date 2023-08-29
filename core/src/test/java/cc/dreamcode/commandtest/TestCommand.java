package cc.dreamcode.commandtest;

import cc.dreamcode.command.DreamCommandImpl;
import cc.dreamcode.command.extension.DefaultExtensionRegistry;
import cc.dreamcode.command.handler.HandlerType;
import cc.dreamcode.commandtest.handler.InvalidUsageHandler;
import lombok.Getter;

@Getter
public class TestCommand extends DreamCommandImpl {

    private final TestCommandRegistry commandRegistry;

    public TestCommand() {
        this.commandRegistry = new TestCommandRegistry(this.getExtensions(), this.getHandlers());
        this.getExtensions().registerExtension(new DefaultExtensionRegistry());
        this.getHandlers().registerHandler(HandlerType.INVALID_USAGE, new InvalidUsageHandler());
    }
}
