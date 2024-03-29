package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public class LongArrayTransformer implements ArrayTransformer<Long> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Long[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<Long[]> transform(@NonNull Class<?> type, @NonNull Object[] objects) {

        final Long[] array = new Long[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Long) objects[index];
        }

        return Optional.of(array);
    }
}
