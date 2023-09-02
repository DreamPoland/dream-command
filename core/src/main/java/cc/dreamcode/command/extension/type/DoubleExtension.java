package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoubleExtension implements ExtensionResolver<Double> {
    @Override
    public @NonNull Double resolveArgument(@NonNull Class<?> argumentClass, @NonNull String input) {
        final Optional<Double> optionalDouble = ParseUtil.parseDouble(input);
        if (!optionalDouble.isPresent()) {
            throw new IllegalArgumentException("Input are not double value: " + input);
        }

        return optionalDouble.get();
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull Class<?> argumentClass, @NonNull String input) {
        return new ArrayList<>();
    }
}
