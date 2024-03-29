package cc.dreamcode.command.resolver.transformer;

import lombok.NonNull;

import java.util.Optional;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EnumTransformer implements ObjectTransformer<Enum> {
    @Override
    public Class<?> getGeneric() {
        return Enum.class;
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
