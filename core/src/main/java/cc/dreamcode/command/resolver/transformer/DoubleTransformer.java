package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class DoubleTransformer implements ObjectTransformer<Double> {
    @Override
    public Class<?> getGeneric() {
        return Double.class;
    }

    @Override
    public Optional<Double> transform(@NonNull Class<?> type, @NonNull String input) {
        return ParseUtil.parseDouble(input);
    }
}
