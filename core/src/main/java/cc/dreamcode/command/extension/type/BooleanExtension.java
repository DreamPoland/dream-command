package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class BooleanExtension implements ExtensionResolver<Boolean> {
    @Override
    public @NonNull Boolean resolveArgument(@NonNull String input) {
        final Optional<Boolean> value = ParseUtil.parseBoolean(input);
        if (!value.isPresent()) {
            throw new IllegalArgumentException("Input are not boolean value: " + input);
        }

        return value.get();
    }
}
