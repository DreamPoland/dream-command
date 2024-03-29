package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class ShortTransformer implements ObjectTransformer<Short> {
    @Override
    public Class<?> getGeneric() {
        return Short.class;
    }

    @Override
    public Optional<Short> transform(@NonNull Class<?> type, @NonNull String input) {
        return ParseUtil.parseShort(input);
    }
}
