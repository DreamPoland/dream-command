package cc.dreamcode.commandtest;

import cc.dreamcode.commandtest.command.SimpleCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class TestCommandRunnable {

    private TestCommand testCommand;

    @BeforeAll
    public void before_all() {
        this.testCommand = new TestCommand();
        this.testCommand.registerCommand(new SimpleCommand());
    }

    @Test
    public void test_command() {
        final TestCommandRegistry registry = this.testCommand.getCommandRegistry();
        registry.parseCommand("simple block");
    }
}
