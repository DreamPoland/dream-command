package cc.dreamcode.command.suggestion.supplier;

import lombok.NonNull;

import java.util.List;

public interface SuggestionSupplier {
    List<String> supply(@NonNull Class<?> paramType);
}
