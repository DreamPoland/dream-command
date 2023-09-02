package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LongExtension implements ExtensionResolver<Long> {
    @Override
    public @NonNull Long resolveArgument(@NonNull Class<?> argumentClass, @NonNull String input) {
        final Optional<Long> optionalLong = ParseUtil.parseLong(input);
        if (!optionalLong.isPresent()) {
            throw new IllegalArgumentException("Input are not long value: " + input);
        }

        return optionalLong.get();
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull Class<?> argumentClass, @NonNull String input) {
        return new ArrayList<>();
    }
}
