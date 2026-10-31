package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public class ShortArrayTransformer implements ArrayTransformer<Short> {
    @Override
    public Class<?> getGeneric() {
        return Short[].class;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Short[].class.isAssignableFrom(type) || short[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<?> transform(@NonNull Class<?> type, @NonNull Object[] objects) {
        if (short[].class.isAssignableFrom(type)) {
            final short[] array = new short[objects.length];
            for (int index = 0; index < objects.length; index++) {
                array[index] = ((Number) objects[index]).shortValue();
            }
            return Optional.of(array);
        }

        final Short[] array = new Short[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Short) objects[index];
        }

        return Optional.of(array);
    }
}
