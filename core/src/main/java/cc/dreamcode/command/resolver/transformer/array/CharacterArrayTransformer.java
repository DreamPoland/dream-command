package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public class CharacterArrayTransformer implements ArrayTransformer<Character> {
    @Override
    public Class<?> getGeneric() {
        return Character[].class;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Character[].class.isAssignableFrom(type) || char[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<?> transform(@NonNull Class<?> type, @NonNull Object[] objects) {
        if (char[].class.isAssignableFrom(type)) {
            final char[] array = new char[objects.length];
            for (int index = 0; index < objects.length; index++) {
                array[index] = (Character) objects[index];
            }
            return Optional.of(array);
        }

        final Character[] array = new Character[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Character) objects[index];
        }

        return Optional.of(array);
    }
}
