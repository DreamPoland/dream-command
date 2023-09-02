package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IntegerExtension implements ExtensionResolver<Integer> {
    @Override
    public @NonNull Integer resolveArgument(@NonNull Class<?> argumentClass, @NonNull String input) {
        final Optional<Integer> optionalInteger = ParseUtil.parseInteger(input);
        if (!optionalInteger.isPresent()) {
            throw new IllegalArgumentException("Input are not int value: " + input);
        }

        return optionalInteger.get();
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull Class<?> argumentClass, @NonNull String input) {
        return new ArrayList<>();
    }
}
