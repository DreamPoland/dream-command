package cc.dreamcode.command;

import cc.dreamcode.command.user.ExampleCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCommand {

    private CommandProvider commandProvider;

    @BeforeEach
    void setUp() {
        this.commandProvider = new CommandProviderImpl(true);
        this.commandProvider.register(new ExampleCommand());
    }

    @Test
    void testCall() {
        String input = "example";
        this.commandProvider.call(input);
    }
}