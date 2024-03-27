package cc.dreamcode.command.resolver;

import cc.dreamcode.command.CommandExtension;
import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.resolver.transformer.CharSequenceTransformer;
import lombok.NonNull;

public class DefaultTransformers implements CommandExtension {

    @Override
    public void register(@NonNull CommandProvider commandProvider) {
        commandProvider.registerTransformer(new CharSequenceTransformer());
    }
}
