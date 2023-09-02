package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShortExtension implements ExtensionResolver<Short> {
    @Override
    public @NonNull Short resolveArgument(@NonNull String input) {
        final Optional<Short> optionalShort = ParseUtil.parseShort(input);
        if (!optionalShort.isPresent()) {
            throw new IllegalArgumentException("Input are not short value: " + input);
        }

        return optionalShort.get();
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull String input) {
        return new ArrayList<>();
    }
}
