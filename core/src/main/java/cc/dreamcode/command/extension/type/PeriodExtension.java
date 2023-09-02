package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PeriodExtension implements ExtensionResolver<Duration> {
    @Override
    public @NonNull Duration resolveArgument(@NonNull String input) {
        final Optional<Duration> optionalPeriod = ParseUtil.parsePeriod(input);
        if (!optionalPeriod.isPresent()) {
            throw new IllegalArgumentException("Input are not duration value: " + input);
        }

        return optionalPeriod.get();
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull String input) {
        return new ArrayList<>();
    }
}
