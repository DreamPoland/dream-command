package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public class BooleanArrayTransformer implements ArrayTransformer<Boolean> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Boolean[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<Boolean[]> transform(@NonNull Class<?> type, @NonNull Object[] objects) {

        final Boolean[] array = new Boolean[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Boolean) objects[index];
        }

        return Optional.of(array);
    }
}
