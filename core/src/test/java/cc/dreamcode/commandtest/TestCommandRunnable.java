package cc.dreamcode.commandtest;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.DreamCommandValidator;
import cc.dreamcode.command.context.CommandInvokeContext;
import cc.dreamcode.commandtest.command.SimpleCommand;
import cc.dreamcode.commandtest.sender.TestSender;
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
        this.parseCommand("simple number info info5 4 no");
    }

    public void parseCommand(@NonNull String input) {
        final CommandInvokeContext commandInvokeContext = new CommandInvokeContext(input);
        final TestSender sender = new TestSender();

        final DreamCommandExecutor executor = this.testCommand.getCommandRegistry().getCommand(new DreamCommandValidator(commandInvokeContext));
        executor.invokeMethod(sender, commandInvokeContext);
        System.out.println(executor.getSuggestion(sender, commandInvokeContext));
    }
}
