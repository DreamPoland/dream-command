package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.command.resolver.ObjectTransformer;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class ByteTransformer implements ObjectTransformer<Byte> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Byte.class.isAssignableFrom(type);
    }

    @Override
    public Optional<Byte> transform(@NonNull Class<?> type, @NonNull String input) {
        return ParseUtil.parseByte(input);
    }
}
