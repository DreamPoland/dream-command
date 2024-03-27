package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.command.resolver.ObjectTransformer;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class CharacterTransformer implements ObjectTransformer<Character> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Character.class.isAssignableFrom(type);
    }

    @Override
    public Optional<Character> transform(@NonNull Class<?> type, @NonNull String input) {

        if (input.length() != 1) {
            return Optional.empty();
        }

        return ParseUtil.parseChar(input);
    }
}
