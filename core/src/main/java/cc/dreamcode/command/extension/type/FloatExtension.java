package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FloatExtension implements ExtensionResolver<Float> {
    @Override
    public @NonNull Float resolveArgument(@NonNull Class<?> argumentClass, @NonNull String input) {
        final Optional<Float> optionalFloat = ParseUtil.parseFloat(input);
        if (!optionalFloat.isPresent()) {
            throw new IllegalArgumentException("Input are not float value: " + input);
        }

        return optionalFloat.get();
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull Class<?> argumentClass, @NonNull String input) {
        return new ArrayList<>();
    }
}
