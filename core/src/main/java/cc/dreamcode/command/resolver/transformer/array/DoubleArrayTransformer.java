package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public class DoubleArrayTransformer implements ArrayTransformer<Double> {
    @Override
    public Class<?> getGeneric() {
        return Double[].class;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Double[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<Double[]> transform(@NonNull Class<?> type, @NonNull Object[] objects) {

        final Double[] array = new Double[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Double) objects[index];
        }

        return Optional.of(array);
    }
}
