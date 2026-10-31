package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public class IntegerArrayTransformer implements ArrayTransformer<Integer> {
    @Override
    public Class<?> getGeneric() {
        return Integer[].class;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Integer[].class.isAssignableFrom(type) || int[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<?> transform(@NonNull Class<?> type, @NonNull Object[] objects) {
        if (int[].class.isAssignableFrom(type)) {
            final int[] array = new int[objects.length];
            for (int index = 0; index < objects.length; index++) {
                array[index] = ((Number) objects[index]).intValue();
            }
            return Optional.of(array);
        }

        final Integer[] array = new Integer[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Integer) objects[index];
        }

        return Optional.of(array);
    }
}
