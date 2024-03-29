package cc.dreamcode.command;

import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import lombok.NonNull;

public interface CommandProvider {

    CommandProviderImpl call(@NonNull CommandSender<?> commandSender, @NonNull String input);

    CommandProviderImpl register(@NonNull CommandBase commandBase);

    CommandProviderImpl registerExtension(@NonNull CommandExtension commandExtension);

    CommandProviderImpl registerTransformer(@NonNull ObjectTransformer<?> objectTransformer);

    CommandProviderImpl registerTransformer(@NonNull ArrayTransformer<?> arrayTransformer);

    CommandProviderImpl unregisterTransformer(@NonNull Class<?> classTransformer, boolean array);

    CommandProviderImpl registerBind(@NonNull BindResolver<?> bindResolver);

    CommandProviderImpl unregisterBind(@NonNull Class<?> bindClass);
}
