package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Executor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

class TestCommand {

    private CommandProvider commandProvider;

    @BeforeEach
    void setUp() {
        this.commandProvider = new CommandProviderImpl(true);
        this.commandProvider.register(new ExampleCommand());
    }

    @Test
    void testCall() {
        String input = "/example example test 1d";
        this.commandProvider.call(input);
    }

    @Command(name = "example", description = "Example command.")
    public static class ExampleCommand implements CommandBase {

        @Executor(pattern = "example test")
        public void example(@Arg Duration duration) {
            System.out.println("example: " + duration);
        }
    }
}