package cc.dreamcode.command.extension;

import lombok.NonNull;

public interface ExtensionResolver<A> {

    @NonNull A resolveArgument(@NonNull String input);
}
