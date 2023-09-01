package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class StringExtension implements ExtensionResolver<String> {
    @Override
    public @NonNull String resolveArgument(@NonNull String input) {
        return input;
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull String input) {
        return new ArrayList<>();
    }
}
