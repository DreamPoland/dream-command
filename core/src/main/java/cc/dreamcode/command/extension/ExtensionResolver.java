package cc.dreamcode.command.extension;

import lombok.NonNull;

import java.util.List;

public interface ExtensionResolver<A> {

    @NonNull Class<A> getClassType();

    @NonNull A resolveArgument(@NonNull Class<?> argumentClass, @NonNull String input) throws IllegalArgumentException;

    @NonNull List<String> getSuggestion(@NonNull Class<?> argumentClass, @NonNull String input);
}
