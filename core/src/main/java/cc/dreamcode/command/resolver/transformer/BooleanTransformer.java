package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.command.resolver.ObjectTransformer;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public class BooleanTransformer implements ObjectTransformer<Boolean> {

    private final List<String> trueValues = ListBuilder.of("true", "yes", "y", "1", "tak", "si");
    private final List<String> falseValues = ListBuilder.of("false", "no", "n", "0", "nie", "nah");

    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Boolean.class.isAssignableFrom(type);
    }

    @Override
    public Optional<Boolean> transform(@NonNull Class<?> type, @NonNull String input) {

        if (this.trueValues.contains(input)) {
            return Optional.of(Boolean.TRUE);
        }

        if (this.falseValues.contains(input)) {
            return Optional.of(Boolean.FALSE);
        }

        return Optional.empty();
    }
}
