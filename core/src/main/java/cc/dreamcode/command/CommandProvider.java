package cc.dreamcode.command;

import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import lombok.NonNull;

import java.util.List;

public interface CommandProvider {

    List<String> getSuggestion(@NonNull String input);

    CommandProviderImpl call(@NonNull CommandSender<?> commandSender, @NonNull String input);

    CommandProviderImpl register(@NonNull CommandBase commandBase);

    CommandProviderImpl registerExtension(@NonNull CommandExtension commandExtension);

    CommandProviderImpl registerTransformer(@NonNull ObjectTransformer<?> objectTransformer);

    CommandProviderImpl registerTransformer(@NonNull ArrayTransformer<?> arrayTransformer);

    CommandProviderImpl unregisterTransformer(@NonNull Class<?> classTransformer, boolean array);

    CommandProviderImpl registerBind(@NonNull BindResolver<?> bindResolver);

    CommandProviderImpl unregisterBind(@NonNull Class<?> bindClass);

    CommandProviderImpl registerSuggestion(@NonNull String key, @NonNull SuggestionSupplier suggestionSupplier);

    CommandProviderImpl unregisterSuggestion(@NonNull String key);

    CommandProviderImpl registerSuggestionFilter(@NonNull String key, @NonNull SuggestionFilter suggestionFilter);

    CommandProviderImpl unregisterSuggestionFilter(@NonNull String key);
}
