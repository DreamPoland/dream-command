package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public class ByteArrayTransformer implements ArrayTransformer<Byte> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Byte[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<Byte[]> transform(@NonNull Class<?> type, @NonNull Object[] objects) {

        final Byte[] array = new Byte[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Byte) objects[index];
        }

        return Optional.of(array);
    }
}
