package cc.dreamcode.command.suggestion;

import cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SuggestionCache {

    private final Map<String, SuggestionSupplier> suggestionMap = new ConcurrentHashMap<>();
    private final Map<String, SuggestionFilter> suggestionFilterMap = new ConcurrentHashMap<>();

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
        this.suggestionMap.remove(key);
    }

    public void removeSuggestionFilter(@NonNull String key) {
        this.suggestionFilterMap.remove(key);
    }
}
