package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BooleanExtension implements ExtensionResolver<Boolean> {
    @Override
    public @NonNull Boolean resolveArgument(@NonNull Class<?> argumentClass, @NonNull String input) {
        return ParseUtil.parseBoolean(input);
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull Class<?> argumentClass, @NonNull String input) {
        return Stream.of("true", "false")
                .collect(Collectors.toList());
    }
}
