package cc.dreamcode.command.suggestion;

import cc.dreamcode.command.annotation.Completion;
import cc.dreamcode.command.annotation.CompletionFilter;
import cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class SuggestionService {

    private final SuggestionCache suggestionCache;

    public List<String> getSuggestion(@NonNull Completion completion) {

        final List<String> suggestions = new ArrayList<>();

        for (String value : completion.value()) {
            final String[] split = value.split(" ");
            if (split.length == 0) {
                continue;
            }

            final String firstValue = split[0];
            final Optional<SuggestionSupplier> optionalSupplier = this.suggestionCache.getSuggestion(firstValue);
            if (!optionalSupplier.isPresent()) {
                suggestions.add(firstValue);
                continue;
            }

            final SuggestionSupplier supplier = optionalSupplier.get();
            final AtomicReference<List<String>> reference = new AtomicReference<>(supplier.supply());

            final CompletionFilter[] completionFilterArray = completion.filter();
            for (CompletionFilter completionFilter : completionFilterArray) {
                Optional<SuggestionFilter> optionalFilter = this.suggestionCache.getSuggestionFilter(completionFilter.name());
                if (!optionalFilter.isPresent()) {
                    throw new RuntimeException("Cannot resolve suggestion-filter by key: " + completionFilter.name());
                }

                final SuggestionFilter suggestionFilter = optionalFilter.get();
                reference.set(suggestionFilter.filter(reference.get(), completionFilter.value()));
            }

            suggestions.addAll(reference.get());
        }

        return suggestions;
    }
}
