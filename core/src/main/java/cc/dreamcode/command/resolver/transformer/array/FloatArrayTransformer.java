package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public class FloatArrayTransformer implements ArrayTransformer<Float> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Float[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<Float[]> transform(@NonNull Class<?> type, @NonNull Object[] objects) {

        final Float[] array = new Float[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Float) objects[index];
        }

        return Optional.of(array);
    }
}
