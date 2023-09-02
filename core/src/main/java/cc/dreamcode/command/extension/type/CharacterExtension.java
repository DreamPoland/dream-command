package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CharacterExtension implements ExtensionResolver<Character> {
    @Override
    public @NonNull Character resolveArgument(@NonNull Class<?> argumentClass, @NonNull String input) {
        final Optional<Character> optionalCharacter = ParseUtil.parseChar(input);
        if (!optionalCharacter.isPresent()) {
            throw new IllegalArgumentException("Input are not character value: " + input);
        }

        return optionalCharacter.get();
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull Class<?> argumentClass, @NonNull String input) {
        return new ArrayList<>();
    }
}
