package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.command.resolver.ObjectTransformer;
import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class FloatTransformer implements ObjectTransformer<Float> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Float.class.isAssignableFrom(type);
    }

    @Override
    public Optional<Float> transform(@NonNull Class<?> type, @NonNull String input) {
        return ParseUtil.parseFloat(input);
    }
}
