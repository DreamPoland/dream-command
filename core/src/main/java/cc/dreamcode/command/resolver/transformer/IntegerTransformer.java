package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;

import java.util.Optional;

public class IntegerTransformer implements ObjectTransformer<Integer> {
    @Override
    public Class<?> getGeneric() {
        return Integer.class;
    }

    @Override
    public Optional<Integer> transform(@NonNull Class<?> type, @NonNull String input) {
        return ParseUtil.parseInteger(input);
    }
}
