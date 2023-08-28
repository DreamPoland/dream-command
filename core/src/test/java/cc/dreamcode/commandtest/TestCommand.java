package cc.dreamcode.commandtest;

import cc.dreamcode.command.DreamCommandImpl;
import cc.dreamcode.command.extension.DefaultExtensionRegistry;
import lombok.Getter;

@Getter
public class TestCommand extends DreamCommandImpl {

    private final TestCommandRegistry commandRegistry;

    public TestCommand() {
        this.commandRegistry = new TestCommandRegistry();
        this.getExtensions().registerExtension(new DefaultExtensionRegistry());
    }
}
