package cc.dreamcode.commandtest;

import cc.dreamcode.command.DreamCommandImpl;
import cc.dreamcode.command.extension.DefaultExtensionRegistry;
import cc.dreamcode.commandtest.bind.TestCommandBindRegistry;
import cc.dreamcode.commandtest.handler.InvalidInputValueHandler;
import cc.dreamcode.commandtest.handler.InvalidSenderTypeHandler;
import cc.dreamcode.commandtest.handler.InvalidUsageHandler;
import cc.dreamcode.commandtest.handler.NoPermissionHandler;
import lombok.Getter;

@Getter
public class TestCommand extends DreamCommandImpl {

    private final TestCommandRegistry commandRegistry;

    public TestCommand() {
        this.commandRegistry = new TestCommandRegistry(this.getExtensions(), this.getHandlers(), this.getBinds());
        this.getExtensions().registerExtension(new DefaultExtensionRegistry());
        this.getBinds().registerBind(new TestCommandBindRegistry());

        this.getHandlers().registerHandler(new InvalidUsageHandler());
        this.getHandlers().registerHandler(new InvalidInputValueHandler());
        this.getHandlers().registerHandler(new NoPermissionHandler());
        this.getHandlers().registerHandler(new InvalidSenderTypeHandler());
    }
}
