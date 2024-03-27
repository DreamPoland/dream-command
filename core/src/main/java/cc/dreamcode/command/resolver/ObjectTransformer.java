package cc.dreamcode.command.resolver;

import lombok.NonNull;

import java.util.Optional;

public interface ObjectTransformer<T> {

    boolean isAssignableFrom(@NonNull Class<?> type);

    Optional<T> transform(@NonNull Class<?> type, @NonNull String input);
}
