package cc.dreamcode.command.extension;

import cc.dreamcode.command.extension.type.BooleanExtension;
import cc.dreamcode.command.extension.type.ByteExtension;
import cc.dreamcode.command.extension.type.CharacterExtension;
import cc.dreamcode.command.extension.type.DoubleExtension;
import cc.dreamcode.command.extension.type.EnumExtension;
import cc.dreamcode.command.extension.type.FloatExtension;
import cc.dreamcode.command.extension.type.IntegerExtension;
import cc.dreamcode.command.extension.type.LongExtension;
import cc.dreamcode.command.extension.type.PeriodExtension;
import cc.dreamcode.command.extension.type.ShortExtension;
import cc.dreamcode.command.extension.type.StringExtension;
import lombok.NonNull;

import java.time.Duration;

public class DefaultExtensionRegistry implements ExtensionRegistry {
    @Override
    public void register(@NonNull ExtensionManager extensionManager) {
        extensionManager.registerExtension(new BooleanExtension(), Boolean.class);
        extensionManager.registerExtension(new BooleanExtension(), boolean.class);
        extensionManager.registerExtension(new ByteExtension(), Byte.class);
        extensionManager.registerExtension(new ByteExtension(), byte.class);
        extensionManager.registerExtension(new CharacterExtension(), Character.class);
        extensionManager.registerExtension(new CharacterExtension(), char.class);
        extensionManager.registerExtension(new DoubleExtension(), Double.class);
        extensionManager.registerExtension(new DoubleExtension(), double.class);
        extensionManager.registerExtension(new FloatExtension(), Float.class);
        extensionManager.registerExtension(new FloatExtension(), float.class);
        extensionManager.registerExtension(new IntegerExtension(), Integer.class);
        extensionManager.registerExtension(new IntegerExtension(), int.class);
        extensionManager.registerExtension(new LongExtension(), Long.class);
        extensionManager.registerExtension(new LongExtension(), long.class);
        extensionManager.registerExtension(new PeriodExtension(), Duration.class);
        extensionManager.registerExtension(new ShortExtension(), Short.class);
        extensionManager.registerExtension(new ShortExtension(), short.class);
        extensionManager.registerExtension(new StringExtension(), String.class);
        extensionManager.registerExtension(new EnumExtension(), Enum.class);
    }
}
