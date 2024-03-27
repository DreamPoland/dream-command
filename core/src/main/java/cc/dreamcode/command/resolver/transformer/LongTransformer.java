package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.command.resolver.ObjectTransformer;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class LongTransformer implements ObjectTransformer<Long> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Long.class.isAssignableFrom(type);
    }

    @Override
    public Optional<Long> transform(@NonNull Class<?> type, @NonNull String input) {
        return ParseUtil.parseLong(input);
    }
}
