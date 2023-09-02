package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ByteExtension implements ExtensionResolver<Byte> {
    @Override
    public @NonNull Byte resolveArgument(@NonNull Class<?> argumentClass, @NonNull String input) {
        final Optional<Byte> optionalByte = ParseUtil.parseByte(input);
        if (!optionalByte.isPresent()) {
            throw new IllegalArgumentException("Input are not byte value: " + input);
        }

        return optionalByte.get();
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull Class<?> argumentClass, @NonNull String input) {
        return new ArrayList<>();
    }
}
