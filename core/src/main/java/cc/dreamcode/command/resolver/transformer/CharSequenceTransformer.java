package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.command.resolver.ObjectTransformer;
import lombok.NonNull;

import java.util.Optional;

public class CharSequenceTransformer implements ObjectTransformer<CharSequence> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return CharSequence.class.isAssignableFrom(type);
    }

    @Override
    public Optional<CharSequence> transform(@NonNull Class<?> type, @NonNull String input) {
        return Optional.of(input);
    }
}
