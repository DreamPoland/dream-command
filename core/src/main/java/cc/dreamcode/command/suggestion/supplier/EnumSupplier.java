package cc.dreamcode.command.suggestion.supplier;

import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumSupplier implements SuggestionSupplier {
    @SuppressWarnings("all")
    @Override
    public List<String> supply(@NonNull Class<?> paramType) {
        if (!Enum.class.isAssignableFrom(paramType)) {
            throw new RuntimeException("Parameter is not Enum class (" + paramType.getSimpleName() + ")");
        }

        final Class<? extends Enum> enumClass = (Class<? extends Enum>) paramType;

        return Arrays.stream(enumClass.getEnumConstants())
                .map(scan -> scan.name().toLowerCase())
                .collect(Collectors.toList());
    }
}
