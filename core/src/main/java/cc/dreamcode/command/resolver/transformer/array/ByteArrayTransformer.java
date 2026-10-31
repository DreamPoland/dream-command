package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public class ByteArrayTransformer implements ArrayTransformer<Byte> {
    @Override
    public Class<?> getGeneric() {
        return Byte[].class;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Byte[].class.isAssignableFrom(type) || byte[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<?> transform(@NonNull Class<?> type, @NonNull Object[] objects) {
        if (byte[].class.isAssignableFrom(type)) {
            final byte[] array = new byte[objects.length];
            for (int index = 0; index < objects.length; index++) {
                array[index] = ((Number) objects[index]).byteValue();
            }
            return Optional.of(array);
        }

        final Byte[] array = new Byte[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Byte) objects[index];
        }

        return Optional.of(array);
    }
}
