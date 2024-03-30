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
        return Short[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<Short[]> transform(@NonNull Class<?> type, @NonNull Object[] objects) {

        final Short[] array = new Short[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Short) objects[index];
        }

        return Optional.of(array);
    }
}
