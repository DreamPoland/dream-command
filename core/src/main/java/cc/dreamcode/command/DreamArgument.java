package cc.dreamcode.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class DreamArgument<C, P> implements DreamCommand<C, P> {

    private final String name;
    private final int arg;

}
