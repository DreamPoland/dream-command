package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Completion;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.OptArg;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.command.annotation.Sender;
import cc.dreamcode.command.result.ResultResolver;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.Getter;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class TestCommand {

    private CommandProvider commandProvider;
    private TestSender testSender;
    private ExampleCommand exampleCommand;

    private final List<String> invalidUsageLogs = new ArrayList<>();
    private final AtomicReference<String> lastInvalidPermission = new AtomicReference<>();
    private final AtomicReference<DreamSender.Type> lastInvalidSender = new AtomicReference<>();
    private final AtomicReference<String> lastInvalidInput = new AtomicReference<>();
    private final List<String> resultLogs = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.invalidUsageLogs.clear();
        this.lastInvalidPermission.set(null);
        this.lastInvalidSender.set(null);
        this.lastInvalidInput.set(null);
        this.resultLogs.clear();

        this.commandProvider = new CommandProviderImpl(true);
        this.commandProvider.registerSuggestion("@all-players", (paramType) ->
                ListBuilder.of("nick1", "nick2", "nick3", "nick4", "nick5"));

        this.testSender = new TestSender();
        this.commandProvider.registerBind(new TestSenderBind());

        this.commandProvider.setInvalidUsageHandler((dreamSender, commandMeta, commandInput) -> {
            this.invalidUsageLogs.add(commandInput.getLabel());
        });

        this.commandProvider.setInvalidPermissionHandler((dreamSender, permission) -> {
            this.lastInvalidPermission.set(permission);
        });

        this.commandProvider.setInvalidSenderHandler((dreamSender, requireTypes) -> {
            this.lastInvalidSender.set(dreamSender.getType());
        });

        this.commandProvider.setInvalidInputHandler((dreamSender, requiringClass, input) -> {
            this.lastInvalidInput.set(input);
        });

        this.commandProvider.registerResult(new ResultResolver() {
            @Override
            public boolean isAssignableFrom(@NonNull Class<?> type) {
                return String.class.isAssignableFrom(type);
            }

            @Override
            public void resolveResult(@NonNull DreamSender<?> sender, @NonNull Class<?> type, @NonNull Object object) {
                resultLogs.add(object.toString());
            }
        });

        this.exampleCommand = new ExampleCommand();
        this.commandProvider.register(this.exampleCommand);
    }

    @Test
    void shouldExecuteEnumCommand() {
        this.commandProvider.call(this.testSender, "/example test1 BRO");
        assertTrue(this.exampleCommand.isEnumExecuted());
        assertEquals(ExampleEnum.BRO, this.exampleCommand.getLastEnum());
    }

    @Test
    void shouldExecutePrimitiveIntArrayCommand() {
        this.commandProvider.call(this.testSender, "/example numbers 10 20 30");
        assertTrue(this.exampleCommand.isNumbersExecuted());
        assertArrayEquals(new int[]{10, 20, 30}, this.exampleCommand.getLastNumbers());
    }

    @Test
    void shouldExecuteEmptyPrimitiveIntArrayCommand() {
        this.commandProvider.call(this.testSender, "/example numbers");
        assertTrue(this.exampleCommand.isNumbersExecuted());
        assertArrayEquals(new int[0], this.exampleCommand.getLastNumbers());
    }

    @Test
    void shouldExecuteSingleArgumentCommand() {
        this.commandProvider.call(this.testSender, "/example hello Notch");
        assertEquals("Notch", this.exampleCommand.getLastSingleArg());
    }

    @Test
    void shouldExecuteOptionalArgumentWhenProvided() {
        this.commandProvider.call(this.testSender, "/example opt Notch 25");
        assertEquals("Notch", this.exampleCommand.getLastOptName());
        assertEquals(Optional.of(25), this.exampleCommand.getLastOptAge());
    }

    @Test
    void shouldExecuteOptionalArgumentWhenOmitted() {
        this.commandProvider.call(this.testSender, "/example opt Notch");
        assertEquals("Notch", this.exampleCommand.getLastOptName());
        assertEquals(Optional.empty(), this.exampleCommand.getLastOptAge());
    }

    @Test
    void shouldHandleResultResolver() {
        this.commandProvider.call(this.testSender, "/example greet Steve");
        assertEquals(1, this.resultLogs.size());
        assertEquals("Hello Steve!", this.resultLogs.get(0));
    }

    @Test
    void shouldHandleInvalidInput() {
        this.commandProvider.call(this.testSender, "/example calculate not_a_number");
        assertEquals("not_a_number", this.lastInvalidInput.get());
    }

    @Test
    void shouldHandlePermissionCheck() {
        TestSender noAdminSender = new TestSender() {
            @Override
            public boolean hasPermission(@NonNull String permission) {
                return permission.equals("example.base");
            }
        };

        this.commandProvider.call(noAdminSender, "/example admin");
        assertEquals("example.admin", this.lastInvalidPermission.get());
    }

    @Test
    void shouldHandleSenderTypeCheck() {
        TestSender clientSender = new TestSender() {
            @Override
            public Type getType() {
                return Type.CLIENT;
            }
        };

        this.commandProvider.call(clientSender, "/example consoleonly");
        assertEquals(DreamSender.Type.CLIENT, this.lastInvalidSender.get());
    }

    @Test
    void shouldProvideSuggestions() {
        List<String> suggestions = this.commandProvider.getSuggestion(this.testSender, "/example type ");
        assertNotNull(suggestions);
        assertTrue(suggestions.contains("bro"));
        assertTrue(suggestions.contains("yes"));
    }

    @Test
    void shouldSuggestSubpathsOnRootSpace() {
        List<String> suggestions = this.commandProvider.getSuggestion(this.testSender, "/example ");
        assertTrue(suggestions.contains("hello"));
        assertTrue(suggestions.contains("opt"));
        assertTrue(suggestions.contains("type"));
        assertFalse(suggestions.contains("<name>"));
    }

    @Test
    void shouldSuggestSubpathOnPrefix() {
        List<String> suggestions = this.commandProvider.getSuggestion(this.testSender, "/example hel");
        assertEquals(1, suggestions.size());
        assertEquals("hello", suggestions.get(0));
    }

    @Test
    void shouldSuggestFirstArgumentAfterSubpath() {
        List<String> suggestions = this.commandProvider.getSuggestion(this.testSender, "/example opt ");
        assertEquals(1, suggestions.size());
        assertEquals("<name>", suggestions.get(0));
    }

    @Test
    void shouldSuggestSecondArgumentAfterFirstArgument() {
        List<String> suggestions = this.commandProvider.getSuggestion(this.testSender, "/example opt Notch ");
        assertEquals(1, suggestions.size());
        assertEquals("[age]", suggestions.get(0));
    }

    @Test
    void shouldHandleBukkitStyleCommandInputWithTrailingEmpty() {
        // Simulates Bukkit tabComplete for "/example " -> args = [""]
        CommandInput rootInput = new CommandInput("example", new String[]{""}, true);
        List<String> rootSuggestions = this.commandProvider.getSuggestion(this.testSender, rootInput);
        assertTrue(rootSuggestions.contains("hello"));
        assertTrue(rootSuggestions.contains("opt"));
        assertFalse(rootSuggestions.contains("<name>"));

        // Simulates Bukkit tabComplete for "/example opt " -> args = ["opt", ""]
        CommandInput optInput = new CommandInput("example", new String[]{"opt", ""}, true);
        List<String> optSuggestions = this.commandProvider.getSuggestion(this.testSender, optInput);
        assertEquals(1, optSuggestions.size());
        assertEquals("<name>", optSuggestions.get(0));

        // Simulates Bukkit tabComplete for "/example opt Notch " -> args = ["opt", "Notch", ""]
        CommandInput optNotchInput = new CommandInput("example", new String[]{"opt", "Notch", ""}, true);
        List<String> optNotchSuggestions = this.commandProvider.getSuggestion(this.testSender, optNotchInput);
        assertEquals(1, optNotchSuggestions.size());
        assertEquals("[age]", optNotchSuggestions.get(0));
    }

    @Test
    void shouldSuggestEnumValuesCaseInsensitive() {
        List<String> suggestions = this.commandProvider.getSuggestion(this.testSender, "/example type B");
        assertTrue(suggestions.contains("bro"));
        assertFalse(suggestions.contains("yes"));
    }

    @Test
    void shouldUnregisterCommand() {
        CommandEntry entry = new CommandEntry("example", new String[0], "Example test command.");
        this.commandProvider.unregister(entry);

        this.commandProvider.call(this.testSender, "/example test1 BRO");
        assertTrue(this.invalidUsageLogs.contains("example"));
    }

    @Permission("example.base")
    @Command(name = "example", description = "Example test command.")
    public static class ExampleCommand implements CommandBase {

        @Getter
        private boolean enumExecuted = false;
        @Getter
        private ExampleEnum lastEnum;

        @Getter
        private boolean numbersExecuted = false;
        @Getter
        private int[] lastNumbers;

        @Getter
        private String lastSingleArg;

        @Getter
        private String lastOptName;
        @Getter
        private Optional<Integer> lastOptAge;

        @Executor(path = {"type", "test1"})
        @Completion(arg = "exampleEnum", value = "@enum")
        void suggestEnum(@OptArg Optional<ExampleEnum> exampleEnum) {
            this.enumExecuted = true;
            this.lastEnum = exampleEnum.orElse(null);
        }

        @Executor(path = "numbers")
        void numbers(@Args int[] numbers) {
            this.numbersExecuted = true;
            this.lastNumbers = numbers;
        }

        @Executor(path = "hello")
        void hello(@Arg String name) {
            this.lastSingleArg = name;
        }

        @Executor(path = "opt")
        void opt(@Arg String name, @OptArg Optional<Integer> age) {
            this.lastOptName = name;
            this.lastOptAge = age;
        }

        @Executor(path = "greet")
        String greet(@Arg String name) {
            return "Hello " + name + "!";
        }

        @Executor(path = "calculate")
        void calculate(@Arg int number) {
        }

        @Permission("example.admin")
        @Executor(path = "admin")
        void admin() {
        }

        @Sender(DreamSender.Type.CONSOLE)
        @Executor(path = "consoleonly")
        void consoleOnly() {
        }
    }
}
