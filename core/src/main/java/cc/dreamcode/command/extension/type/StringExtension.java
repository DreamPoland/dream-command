package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import lombok.NonNull;

public class StringExtension implements ExtensionResolver<String> {
    @Override
    public @NonNull String resolveArgument(@NonNull String input) {
        return input;
    }
}
