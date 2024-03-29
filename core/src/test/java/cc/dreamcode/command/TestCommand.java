package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.OptArg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

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
        String input = "/example optional test test test";

        for (int i = 0; i < 100; i++) {
            this.commandProvider.call(this.testSender, input);
        }
    }

    @Command(name = "example", description = "Example command.")
    public static class ExampleCommand implements CommandBase {

        @Executor(path = "optional")
        public void optionalMethod(@Arg String test, @OptArg(generic = Duration.class) Optional<Duration> optionalTest) {
            System.out.println("OPTIONAL - " + optionalTest);
        }
    }
}