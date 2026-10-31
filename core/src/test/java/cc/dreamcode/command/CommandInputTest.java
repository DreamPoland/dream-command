package cc.dreamcode.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandInputTest {

    @Test
    void shouldParseSimpleCommand() {
        CommandInput input = new CommandInput("/help");
        assertEquals("help", input.getLabel());
        assertEquals(0, input.getArguments().length);
        assertFalse(input.isSpaceAtTheEnd());
    }

    @Test
    void shouldParseCommandWithoutLeadingSlash() {
        CommandInput input = new CommandInput("help");
        assertEquals("help", input.getLabel());
        assertEquals(0, input.getArguments().length);
    }

    @Test
    void shouldParseMultipleLeadingSlashes() {
        CommandInput input = new CommandInput("//wand");
        assertEquals("wand", input.getLabel());
        assertEquals(0, input.getArguments().length);
    }

    @Test
    void shouldPreserveSlashesInArguments() {
        CommandInput input = new CommandInput("/broadcast https://example.com/api/v1/test 12/12/2026");
        assertEquals("broadcast", input.getLabel());
        assertEquals(2, input.getArguments().length);
        assertEquals("https://example.com/api/v1/test", input.getArguments()[0]);
        assertEquals("12/12/2026", input.getArguments()[1]);
    }

    @Test
    void shouldHandleMultipleSpacesBetweenArguments() {
        CommandInput input = new CommandInput("/msg   Notch    hello   world");
        assertEquals("msg", input.getLabel());
        assertEquals(3, input.getArguments().length);
        assertEquals("Notch", input.getArguments()[0]);
        assertEquals("hello", input.getArguments()[1]);
        assertEquals("world", input.getArguments()[2]);
    }

    @Test
    void shouldDetectSpaceAtTheEnd() {
        CommandInput input = new CommandInput("/help ");
        assertTrue(input.isSpaceAtTheEnd());
        assertEquals("help", input.getLabel());
        assertEquals(0, input.getArguments().length);

        CommandInput inputWithArgs = new CommandInput("/msg Notch ");
        assertTrue(inputWithArgs.isSpaceAtTheEnd());
        assertEquals("msg", inputWithArgs.getLabel());
        assertEquals(1, inputWithArgs.getArguments().length);
        assertEquals("Notch", inputWithArgs.getArguments()[0]);
    }

    @Test
    void shouldThrowOnEmptyInput() {
        assertThrows(RuntimeException.class, () -> new CommandInput(""));
        assertThrows(RuntimeException.class, () -> new CommandInput("/"));
        assertThrows(RuntimeException.class, () -> new CommandInput("   "));
    }

    @Test
    void shouldReturnCorrectParamsArray() {
        CommandInput input = new CommandInput("/tp Notch 100 64 200");
        String[] params = input.getParams();
        assertArrayEquals(new String[]{"tp", "Notch", "100", "64", "200"}, params);
    }
}
