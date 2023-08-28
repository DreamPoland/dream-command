package cc.dreamcode.commandtest;

import cc.dreamcode.command.path.CommandPath;
import cc.dreamcode.commandtest.command.SimpleCommand;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class TestCommandRunnable {

    private TestCommand testCommand;

    @BeforeAll
    public void before_all() {
        this.testCommand = new TestCommand();
        this.testCommand.getCommandRegistry().registerCommand(new SimpleCommand());
    }

    @Test
    public void test_command() {
        this.parseCommand("simple block kox2");
    }

    public void parseCommand(@NonNull String input) {
        final CommandPath commandPath = new CommandPath(input);
        this.testCommand.getCommandRegistry().getCommand(commandPath)
                .invokeMethod(this.testCommand.getExtensions(), commandPath);
    }
}
