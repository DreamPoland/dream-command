package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class IntegerExtension implements ExtensionResolver<Integer> {
    @Override
    public @NonNull Integer resolveArgument(@NonNull String input) {
        final Optional<Integer> integer = ParseUtil.parseInteger(input);
        if (!integer.isPresent()) {
            throw new IllegalArgumentException("Input are not int value: " + input);
        }

        return integer.get();
    }
}
