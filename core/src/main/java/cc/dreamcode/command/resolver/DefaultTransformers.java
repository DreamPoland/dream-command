package cc.dreamcode.command.resolver;

import cc.dreamcode.command.CommandExtension;
import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.resolver.transformer.BigDecimalTransformer;
import cc.dreamcode.command.resolver.transformer.BigIntegerTransformer;
import cc.dreamcode.command.resolver.transformer.BooleanTransformer;
import cc.dreamcode.command.resolver.transformer.ByteTransformer;
import cc.dreamcode.command.resolver.transformer.CharacterTransformer;
import cc.dreamcode.command.resolver.transformer.DurationTransformer;
import cc.dreamcode.command.resolver.transformer.EnumTransformer;
import cc.dreamcode.command.resolver.transformer.FloatTransformer;
import cc.dreamcode.command.resolver.transformer.IntegerTransformer;
import cc.dreamcode.command.resolver.transformer.LongTransformer;
import cc.dreamcode.command.resolver.transformer.ShortTransformer;
import cc.dreamcode.command.resolver.transformer.StringTransformer;
import cc.dreamcode.command.resolver.transformer.array.BigDecimalArrayTransformer;
import cc.dreamcode.command.resolver.transformer.array.BigIntegerArrayTransformer;
import cc.dreamcode.command.resolver.transformer.array.BooleanArrayTransformer;
import cc.dreamcode.command.resolver.transformer.array.ByteArrayTransformer;
import cc.dreamcode.command.resolver.transformer.array.CharacterArrayTransformer;
import cc.dreamcode.command.resolver.transformer.array.DurationArrayTransformer;
import cc.dreamcode.command.resolver.transformer.array.FloatArrayTransformer;
import cc.dreamcode.command.resolver.transformer.array.IntegerArrayTransformer;
import cc.dreamcode.command.resolver.transformer.array.LongArrayTransformer;
import cc.dreamcode.command.resolver.transformer.array.ShortArrayTransformer;
import cc.dreamcode.command.resolver.transformer.array.StringArrayTransformer;
import lombok.NonNull;

public class DefaultTransformers implements CommandExtension {

    @Override
    public void register(@NonNull CommandProvider commandProvider) {
        commandProvider.registerTransformer(new StringTransformer());
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

        commandProvider.registerTransformer(new StringArrayTransformer());
        commandProvider.registerTransformer(new BigDecimalArrayTransformer());
        commandProvider.registerTransformer(new BigIntegerArrayTransformer());
        commandProvider.registerTransformer(new BooleanArrayTransformer());
        commandProvider.registerTransformer(new ByteArrayTransformer());
        commandProvider.registerTransformer(new CharacterArrayTransformer());
        commandProvider.registerTransformer(new DurationArrayTransformer());
        commandProvider.registerTransformer(new FloatArrayTransformer());
        commandProvider.registerTransformer(new IntegerArrayTransformer());
        commandProvider.registerTransformer(new LongArrayTransformer());
        commandProvider.registerTransformer(new ShortArrayTransformer());
    }
}
