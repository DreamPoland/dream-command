package cc.dreamcode.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class ArgumentHandler<C, P> implements CommandPlatform<C, P> {

    private final String name;
    private final int arg;

}
