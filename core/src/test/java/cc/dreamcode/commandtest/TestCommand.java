package cc.dreamcode.commandtest;

import cc.dreamcode.command.DreamCommandImpl;
import lombok.Getter;

@Getter
public class TestCommand extends DreamCommandImpl {

    private final TestCommandRegistry commandRegistry;

    public TestCommand() {
        this.commandRegistry = new TestCommandRegistry();
    }
}
