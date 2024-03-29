package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Executor;
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
        String input = "/example test test2 te3";

        for (int i = 0; i < 100; i++) {
            this.commandProvider.call(this.testSender, input);
        }
    }

    @Command(name = "example", description = "Example command.")
    public static class ExampleCommand implements CommandBase {

        @Executor()
        public void emptyMethod(TestSender testSender) {
            System.out.println(testSender.getName() + " empty");
        }

        @Executor()
        public void argMethod(TestSender testSender, @Arg String text) {
            System.out.println(testSender.getName() + " +1 " + text);
        }

        @Executor(path = "test")
        public void pathMethod(TestSender testSender) {
            System.out.println(testSender.getName() + " + test");
        }

        @Executor(path = "test test2")
        public void dupPathMethod(TestSender testSender, @Arg String test) {
            System.out.println(testSender.getName() + " + test duo " + test);
        }
    }
}