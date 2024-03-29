package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.math.BigInteger;
import java.util.Optional;

public class BigIntegerArrayTransformer implements ArrayTransformer<BigInteger> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return BigInteger[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<BigInteger[]> transform(@NonNull Class<?> type, @NonNull Object[] objects) {

        final BigInteger[] array = new BigInteger[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (BigInteger) objects[index];
        }

        return Optional.of(array);
    }
}
