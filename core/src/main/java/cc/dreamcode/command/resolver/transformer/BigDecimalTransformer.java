package cc.dreamcode.command.resolver.transformer;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Optional;

public class BigDecimalTransformer implements ObjectTransformer<BigDecimal> {
    @Override
    public Class<?> getGeneric() {
        return BigDecimal.class;
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
