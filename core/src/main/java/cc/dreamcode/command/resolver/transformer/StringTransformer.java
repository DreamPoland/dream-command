package cc.dreamcode.command.resolver.transformer;

import lombok.NonNull;

import java.util.Optional;

public class StringTransformer implements ObjectTransformer<String> {
    @Override
    public Class<?> getGeneric() {
        return String.class;
    }

    @Override
    public Optional<String> transform(@NonNull Class<?> type, @NonNull String input) {
        return Optional.of(input);
    }
}
