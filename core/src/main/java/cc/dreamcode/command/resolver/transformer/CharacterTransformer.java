package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class CharacterTransformer implements ObjectTransformer<Character> {
    @Override
    public Class<?> getGeneric() {
        return Character.class;
    }

    @Override
    public Optional<Character> transform(@NonNull Class<?> type, @NonNull String input) {

        if (input.length() != 1) {
            return Optional.empty();
        }

        return ParseUtil.parseChar(input);
    }
}
