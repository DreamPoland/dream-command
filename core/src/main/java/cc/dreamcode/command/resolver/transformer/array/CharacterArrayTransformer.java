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
        return Character[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<Character[]> transform(@NonNull Class<?> type, @NonNull Object[] objects) {

        final Character[] array = new Character[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Character) objects[index];
        }

        return Optional.of(array);
    }
}
