package cc.dreamcode.commandtest;

import cc.dreamcode.command.DreamCommandValidator;
import cc.dreamcode.command.context.CommandInvokeContext;
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
        this.parseCommand("simple player info test");
    }

    public void parseCommand(@NonNull String input) {
        final CommandInvokeContext commandInvokeContext = new CommandInvokeContext(input);
        final TestCommandSender sender = new TestCommandSender();

        this.testCommand.getCommandRegistry().getCommand(new DreamCommandValidator(commandInvokeContext))
                .invokeMethod(sender, commandInvokeContext);
    }
}
