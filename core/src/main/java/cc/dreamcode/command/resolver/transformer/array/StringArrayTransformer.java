package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public class StringArrayTransformer implements ArrayTransformer<String> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return String[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<String[]> transform(@NonNull Class<?> type, @NonNull Object[] objects) {

        final String[] array = new String[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (String) objects[index];
        }

        return Optional.of(array);
    }
}
