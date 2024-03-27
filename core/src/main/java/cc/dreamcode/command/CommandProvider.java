package cc.dreamcode.command;

import cc.dreamcode.command.resolver.ObjectTransformer;
import lombok.NonNull;

public interface CommandProvider {

    CommandProviderImpl call(@NonNull String input);

    CommandProviderImpl register(@NonNull CommandBase commandBase);

    CommandProviderImpl registerExtension(@NonNull CommandExtension commandExtension);

    CommandProviderImpl registerTransformer(@NonNull ObjectTransformer<?> objectTransformer);

    CommandProviderImpl unregisterTransformer(@NonNull Class<?> classTransformer);
}
