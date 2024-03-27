package cc.dreamcode.command.resolver;

import lombok.NonNull;

public interface ObjectTransformer<T> {

    boolean isAssignableFrom(@NonNull Class<?> type);

    T transform(@NonNull String input);
}
