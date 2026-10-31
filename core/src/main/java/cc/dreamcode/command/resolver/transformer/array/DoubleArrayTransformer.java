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
        return Double[].class.isAssignableFrom(type) || double[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<?> transform(@NonNull Class<?> type, @NonNull Object[] objects) {
        if (double[].class.isAssignableFrom(type)) {
            final double[] array = new double[objects.length];
            for (int index = 0; index < objects.length; index++) {
                array[index] = ((Number) objects[index]).doubleValue();
            }
            return Optional.of(array);
        }

        final Double[] array = new Double[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Double) objects[index];
        }

        return Optional.of(array);
    }
}
