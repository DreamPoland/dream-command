package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public class FloatArrayTransformer implements ArrayTransformer<Float> {
    @Override
    public Class<?> getGeneric() {
        return Float[].class;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Float[].class.isAssignableFrom(type) || float[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<?> transform(@NonNull Class<?> type, @NonNull Object[] objects) {
        if (float[].class.isAssignableFrom(type)) {
            final float[] array = new float[objects.length];
            for (int index = 0; index < objects.length; index++) {
                array[index] = ((Number) objects[index]).floatValue();
            }
            return Optional.of(array);
        }

        final Float[] array = new Float[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Float) objects[index];
        }

        return Optional.of(array);
    }
}
