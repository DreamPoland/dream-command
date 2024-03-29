package cc.dreamcode.command.resolver.transformer;

import lombok.NonNull;

import java.math.BigInteger;
import java.util.Optional;

public class BigIntegerTransformer implements ObjectTransformer<BigInteger> {
    @Override
    public Class<?> getGeneric() {
        return BigInteger.class;
    }

    @Override
    public Optional<BigInteger> transform(@NonNull Class<?> type, @NonNull String input) {
        try {
            BigInteger bigInteger = new BigInteger(input);
            return Optional.of(bigInteger);
        }
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
