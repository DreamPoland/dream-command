package cc.dreamcode.command;

import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.handler.InvalidInputHandler;
import cc.dreamcode.command.handler.InvalidPermissionHandler;
import cc.dreamcode.command.handler.InvalidSenderHandler;
import cc.dreamcode.command.handler.InvalidUsageHandler;
import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import lombok.NonNull;

import java.util.List;

public interface CommandProvider {

    List<String> getSuggestion(@NonNull String input);

    List<String> getSuggestion(@NonNull CommandInput commandInput);

    CommandProviderImpl call(@NonNull DreamSender<?> dreamSender, @NonNull String input);

    CommandProviderImpl call(@NonNull DreamSender<?> dreamSender, @NonNull CommandInput commandInput);

    CommandProviderImpl register(@NonNull CommandBase commandBase);

    CommandProviderImpl unregister(@NonNull CommandContext commandContext);

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

    InvalidPermissionHandler getInvalidPermissionHandler();

    CommandProviderImpl setInvalidPermissionHandler(@NonNull InvalidPermissionHandler invalidPermissionHandler);

    InvalidSenderHandler getInvalidSenderHandler();

    CommandProviderImpl setInvalidSenderHandler(@NonNull InvalidSenderHandler invalidSenderHandler);

    InvalidUsageHandler getInvalidUsageHandler();

    CommandProviderImpl setInvalidUsageHandler(@NonNull InvalidUsageHandler invalidUsageHandler);

    InvalidInputHandler getInvalidInputHandler();

    CommandProviderImpl setInvalidInputHandler(@NonNull InvalidInputHandler invalidInputHandler);

    CommandRegistry getCommandRegistry();

    CommandProviderImpl setCommandRegistry(@NonNull CommandRegistry commandRegistry);
}
