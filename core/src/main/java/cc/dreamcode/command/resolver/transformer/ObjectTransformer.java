package cc.dreamcode.command.resolver.transformer;

import lombok.NonNull;

import java.util.Optional;

public interface ObjectTransformer<T> {

    Class<?> getGeneric();

    default boolean isAssignableFrom(@NonNull Class<?> type) {
        return this.getGeneric().isAssignableFrom(type);
    }

    Optional<T> transform(@NonNull Class<?> type, @NonNull String input);
}
