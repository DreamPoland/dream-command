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
        return Integer[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<Integer[]> transform(@NonNull Class<?> type, @NonNull Object[] objects) {

        final Integer[] array = new Integer[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Integer) objects[index];
        }

        return Optional.of(array);
    }
}
