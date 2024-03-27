package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.command.resolver.ObjectTransformer;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class ShortTransformer implements ObjectTransformer<Short> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Short.class.isAssignableFrom(type);
    }

    @Override
    public Optional<Short> transform(@NonNull Class<?> type, @NonNull String input) {
        return ParseUtil.parseShort(input);
    }
}
