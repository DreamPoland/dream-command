package cc.dreamcode.command.resolver;

import cc.dreamcode.command.CommandExtension;
import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.resolver.transformer.BigDecimalTransformer;
import cc.dreamcode.command.resolver.transformer.BigIntegerTransformer;
import cc.dreamcode.command.resolver.transformer.BooleanTransformer;
import cc.dreamcode.command.resolver.transformer.ByteTransformer;
import cc.dreamcode.command.resolver.transformer.CharSequenceTransformer;
import cc.dreamcode.command.resolver.transformer.CharacterTransformer;
import cc.dreamcode.command.resolver.transformer.DurationTransformer;
import cc.dreamcode.command.resolver.transformer.EnumTransformer;
import cc.dreamcode.command.resolver.transformer.FloatTransformer;
import cc.dreamcode.command.resolver.transformer.IntegerTransformer;
import cc.dreamcode.command.resolver.transformer.LongTransformer;
import cc.dreamcode.command.resolver.transformer.ShortTransformer;
import lombok.NonNull;

public class DefaultTransformers implements CommandExtension {

    @Override
    public void register(@NonNull CommandProvider commandProvider) {
        commandProvider.registerTransformer(new CharSequenceTransformer());
        commandProvider.registerTransformer(new BigDecimalTransformer());
        commandProvider.registerTransformer(new BigIntegerTransformer());
        commandProvider.registerTransformer(new BooleanTransformer());
        commandProvider.registerTransformer(new ByteTransformer());
        commandProvider.registerTransformer(new CharacterTransformer());
        commandProvider.registerTransformer(new DurationTransformer());
        commandProvider.registerTransformer(new EnumTransformer());
        commandProvider.registerTransformer(new FloatTransformer());
        commandProvider.registerTransformer(new IntegerTransformer());
        commandProvider.registerTransformer(new LongTransformer());
        commandProvider.registerTransformer(new ShortTransformer());
    }
}
