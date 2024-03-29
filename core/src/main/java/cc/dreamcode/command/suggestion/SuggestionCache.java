package cc.dreamcode.command.suggestion;

import cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SuggestionCache {

    private final Map<String, SuggestionSupplier> suggestionMap = new HashMap<>();
    private final Map<String, SuggestionFilter> suggestionFilterMap = new HashMap<>();

    public Optional<SuggestionSupplier> getSuggestion(@NonNull String key) {
        return Optional.ofNullable(this.suggestionMap.get(key));
    }

    public Optional<SuggestionFilter> getSuggestionFilter(@NonNull String key) {
        return Optional.ofNullable(this.suggestionFilterMap.get(key));
    }

    public void addSuggestion(@NonNull String key, @NonNull SuggestionSupplier supplier) {
        this.suggestionMap.put(key, supplier);
    }

    public void addSuggestionFilter(@NonNull String key, @NonNull SuggestionFilter suggestionFilter) {
        this.suggestionFilterMap.put(key, suggestionFilter);
    }

    public void removeSuggestion(@NonNull String key) {
        if (!this.suggestionMap.containsKey(key)) {
            return;
        }

        this.suggestionMap.remove(key);
    }

    public void removeSuggestionFilter(@NonNull String key) {
        if (!this.suggestionFilterMap.containsKey(key)) {
            return;
        }

        this.suggestionFilterMap.remove(key);
    }

}
