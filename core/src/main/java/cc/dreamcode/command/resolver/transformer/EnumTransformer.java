package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.command.resolver.ObjectTransformer;
import lombok.NonNull;

import java.util.Optional;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EnumTransformer implements ObjectTransformer<Enum> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Enum.class.isAssignableFrom(type);
    }

    @Override
    public Optional<Enum> transform(@NonNull Class<?> type, @NonNull String input) {
        final Class<? extends Enum> enumClass = (Class<? extends Enum>) type;

        try {
            return Optional.of(Enum.valueOf(enumClass, input));
        }
        catch (Exception e) {
            for (Enum enumConstant : enumClass.getEnumConstants()) {
                if (!enumConstant.name().equalsIgnoreCase(input)) {
                    continue;
                }

                return Optional.of(enumConstant);
            }
        }

        return Optional.empty();
    }
}
