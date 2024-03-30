package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.util.Optional;

public interface ArrayTransformer<T> {

    Class<?> getGeneric();

    boolean isAssignableFrom(@NonNull Class<?> type);

    Optional<T[]> transform(@NonNull Class<?> type, @NonNull Object[] objects);
}
