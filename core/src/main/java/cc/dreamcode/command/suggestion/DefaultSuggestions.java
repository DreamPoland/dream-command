package cc.dreamcode.command.suggestion;

import cc.dreamcode.command.CommandExtension;
import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.suggestion.filter.LimitFilter;
import cc.dreamcode.command.suggestion.supplier.EnumSupplier;
import lombok.NonNull;

public class DefaultSuggestions implements CommandExtension {
    @Override
    public void register(@NonNull CommandProvider commandProvider) {
        commandProvider.registerSuggestionFilter("limit", new LimitFilter());
        commandProvider.registerSuggestion("@enum", new EnumSupplier());
    }
}
