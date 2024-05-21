package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Completion;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.OptArg;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.utilities.builder.ListBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

class TestCommand {

    private CommandProvider commandProvider;
    private TestSender testSender;

    @BeforeEach
    void setUp() {
        this.commandProvider = new CommandProviderImpl(true);
        this.commandProvider.registerSuggestion("@all-players", (paramType) -> ListBuilder.of("nick1", "nick2", "nick3", "nick4", "nick5", "nick6", "nick7", "nick8"));

        this.testSender = new TestSender();
        this.commandProvider.registerBind(new TestSenderBind());

        this.commandProvider.setInvalidUsageHandler((dreamSender, commandMeta, commandInput) -> {
            if (!commandMeta.isPresent()) {
                System.out.println("Brak suggesti.");
                return;
            }

            for (CommandPathMeta filteredCommandPath : commandMeta.get().getFilteredCommandPaths(dreamSender)) {
                System.out.println("- " + filteredCommandPath.getUsage());
            }
        });

        this.commandProvider.register(new ExampleCommand());
    }

    @Test
    void testCall() {
        String input = "/examplb";
        this.commandProvider.call(this.testSender, input);
    }

    @Test
    void testSuggestion() {
        String input = "/example holo add test t sfg sfg";
        System.out.println(this.commandProvider.getSuggestion(this.testSender, input));
    }

    @Permission("test")
    @Command(name = "example", description = "Example command.")
    public static class ExampleCommand implements CommandBase {

        @Executor(path = "type")
        @Completion(arg = "exampleEnum", value = "@enum")
        void suggestEnum(@OptArg(generic = ExampleEnum.class) Optional<ExampleEnum> exampleEnum) {
            System.out.println(exampleEnum.get().equals(ExampleEnum.BRO));
        }

        @Executor(path = "typesafe")
        @Completion(arg = "exampleEnum", value = "notakkk")
        void suggestSafeEnum(@OptArg(generic = ExampleEnum.class) Optional<ExampleEnum> exampleEnum) {
            System.out.println(exampleEnum.get().equals(ExampleEnum.BRO));
        }

        @Executor(path = "holo add")
        void holo(@Arg(value = "id") String id, @Args(value = "text", min = 1) String[] text) {
            System.out.println(id);
            System.out.println(Arrays.toString(text));
        }

        @Executor(path = "holo add testowo")
        void holo3(@Arg(value = "id") String id, @Arg(value = "id4") String id4, @Args(value = "text", min = 2) String[] text) {

        }
    }
}