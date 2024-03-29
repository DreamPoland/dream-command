package cc.dreamcode.command.suggestion.filter;

import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LimitFilter implements SuggestionFilter {
    @Override
    public List<String> filter(@NonNull List<String> suggestions, @NonNull String data) {

        Optional<Integer> optionalInteger = ParseUtil.parseInteger(data);
        if (!optionalInteger.isPresent()) {
            throw new RuntimeException("Limit value is not Integer");
        }

        final int limit = optionalInteger.get();
        return suggestions.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
