package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class ByteTransformer implements ObjectTransformer<Byte> {
    @Override
    public Class<?> getGeneric() {
        return Byte.class;
    }

    @Override
    public Optional<Byte> transform(@NonNull Class<?> type, @NonNull String input) {
        return ParseUtil.parseByte(input);
    }
}
