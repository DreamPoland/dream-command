package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.command.resolver.ObjectTransformer;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Optional;

public class BigDecimalTransformer implements ObjectTransformer<BigDecimal> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return BigDecimal.class.isAssignableFrom(type);
    }

    @Override
    public Optional<BigDecimal> transform(@NonNull Class<?> type, @NonNull String input) {
        try {
            BigDecimal bigDecimal = new BigDecimal(input);
            return Optional.of(bigDecimal);
        }
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
