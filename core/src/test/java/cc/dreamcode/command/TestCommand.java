package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Completion;
import cc.dreamcode.command.annotation.CompletionFilter;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.OptArg;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.utilities.builder.ListBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class TestCommand {

    private CommandProvider commandProvider;
    private TestSender testSender;

    @BeforeEach
    void setUp() {
        this.commandProvider = new CommandProviderImpl(true);
        this.commandProvider.registerSuggestion("all-players", () -> ListBuilder.of("nick1", "nick2", "nick3", "nick4", "nick5", "nick6", "nick7", "nick8"));

        this.testSender = new TestSender();
        this.commandProvider.registerBind(new TestSenderBind());

        this.commandProvider.register(new ExampleCommand());
    }

    @Test
    void testCall() {
        String input = "/example nick3";
        this.commandProvider.call(this.testSender, input);
    }

    @Test
    void testSuggestion() {
        String input = "/example t t t t t t";
        System.out.println(this.commandProvider.getSuggestion(input));
    }

    @Permission(name = "example.permission")
    @Command(name = "example", description = "Example command.")
    public static class ExampleCommand implements CommandBase {

        @Executor()
        @Permission(name = "example.permission")
        @Completion(arg = "test", value = "all-players", filter = @CompletionFilter(name = "limit", value = "5"))
        public void optionalMethod(
                @Arg(name = "test") String test2,
                @OptArg(name = "optional-test") String optionalTest,
                @Args(name = "argsmen", min = 1, max = 5) String[] args
        ) {

            System.out.println("OPTIONAL - " + optionalTest);
            System.out.println(Arrays.toString(args));
        }
    }
}