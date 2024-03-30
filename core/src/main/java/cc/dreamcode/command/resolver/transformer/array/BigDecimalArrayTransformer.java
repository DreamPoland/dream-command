package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Optional;

public class BigDecimalArrayTransformer implements ArrayTransformer<BigDecimal> {
    @Override
    public Class<?> getGeneric() {
        return BigDecimal[].class;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return BigDecimal[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<BigDecimal[]> transform(@NonNull Class<?> type, @NonNull Object[] objects) {

        final BigDecimal[] array = new BigDecimal[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (BigDecimal) objects[index];
        }

        return Optional.of(array);
    }
}
