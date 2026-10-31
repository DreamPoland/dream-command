package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public class LongArrayTransformer implements ArrayTransformer<Long> {
    @Override
    public Class<?> getGeneric() {
        return Long[].class;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Long[].class.isAssignableFrom(type) || long[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<?> transform(@NonNull Class<?> type, @NonNull Object[] objects) {
        if (long[].class.isAssignableFrom(type)) {
            final long[] array = new long[objects.length];
            for (int index = 0; index < objects.length; index++) {
                array[index] = ((Number) objects[index]).longValue();
            }
            return Optional.of(array);
        }

        final Long[] array = new Long[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Long) objects[index];
        }

        return Optional.of(array);
    }
}
