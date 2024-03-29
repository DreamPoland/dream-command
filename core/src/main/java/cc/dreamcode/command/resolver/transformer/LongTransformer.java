package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class LongTransformer implements ObjectTransformer<Long> {
    @Override
    public Class<?> getGeneric() {
        return Long.class;
    }

    @Override
    public Optional<Long> transform(@NonNull Class<?> type, @NonNull String input) {
        return ParseUtil.parseLong(input);
    }
}
