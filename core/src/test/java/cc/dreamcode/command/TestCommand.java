package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.OptArg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCommand {

    private CommandProvider commandProvider;
    private TestSender testSender;

    @BeforeEach
    void setUp() {
        this.commandProvider = new CommandProviderImpl(true);
        this.commandProvider.register(new ExampleCommand());

        this.testSender = new TestSender();
        this.commandProvider.registerBind(new TestSenderBind());
    }

    @Test
    void testCall() {
        String input = "/example test";

        this.commandProvider.call(this.testSender, input);
    }

    @Command(name = "example", description = "Example command.")
    public static class ExampleCommand implements CommandBase {

        @Executor()
        public void optionalMethod(@Arg(name = "test") String test2, @OptArg(name = "optional-test") String optionalTest) {

            System.out.println("OPTIONAL - " + optionalTest);
        }
    }
}