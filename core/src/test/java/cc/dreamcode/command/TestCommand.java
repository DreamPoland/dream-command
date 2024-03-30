package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Completion;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.utilities.builder.ListBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCommand {

    private CommandProvider commandProvider;
    private TestSender testSender;

    @BeforeEach
    void setUp() {
        this.commandProvider = new CommandProviderImpl(true);
        this.commandProvider.registerSuggestion("@all-players", (paramType) -> ListBuilder.of("nick1", "nick2", "nick3", "nick4", "nick5", "nick6", "nick7", "nick8"));

        this.testSender = new TestSender();
        this.commandProvider.registerBind(new TestSenderBind());

        this.commandProvider.register(new ExampleCommand());
    }

    @Test
    void testCall() {
        String input = "/example type nah";
        this.commandProvider.call(this.testSender, input);
    }

    @Test
    void testSuggestion() {
        String input = "/example type n";
        System.out.println(this.commandProvider.getSuggestion(input));
    }

    @Permission(name = "example.permission")
    @Command(name = "example", description = "Example command.")
    public static class ExampleCommand implements CommandBase {

        @Executor(path = "type")
        @Completion(arg = "exampleEnum", value = "@enum")
        void suggestEnum(@Arg ExampleEnum exampleEnum) {
            System.out.println(exampleEnum);
        }
    }
}