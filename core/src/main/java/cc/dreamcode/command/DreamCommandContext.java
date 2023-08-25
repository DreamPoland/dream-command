package cc.dreamcode.command;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
public class DreamCommandContext {

    private final String name;
    private final String description;
    private final List<String> aliases;
}
