package cc.dreamcode.command.suggestion.filter;

import lombok.NonNull;

import java.util.List;

public interface SuggestionFilter {

    List<String> filter(@NonNull List<String> suggestions, @NonNull String data);
}
