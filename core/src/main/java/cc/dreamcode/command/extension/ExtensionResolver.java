package cc.dreamcode.command.extension;

import lombok.NonNull;

import java.util.List;

public interface ExtensionResolver<A> {

    @NonNull A resolveArgument(@NonNull String input) throws IllegalArgumentException;

    @NonNull List<String> getSuggestion(@NonNull String input);
}
