package cc.dreamcode.command.extension.type;

import cc.dreamcode.command.extension.ExtensionResolver;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EnumExtension implements ExtensionResolver<Enum> {
    @Override
    public @NonNull Class<Enum> getClassType() {
        return Enum.class;
    }

    @Override
    public @NonNull Enum resolveArgument(@NonNull Class<?> argumentClass, @NonNull String input) throws IllegalArgumentException {
        final Class<? extends Enum> enumClass = (Class<? extends Enum>) argumentClass;

        try {
            return Enum.valueOf(enumClass, input);
        }
        catch (Exception e) {
            for (Enum enumConstant : enumClass.getEnumConstants()) {
                if (!enumConstant.name().equalsIgnoreCase(input)) {
                    continue;
                }

                return enumConstant;
            }
        }

        throw new IllegalArgumentException("Input are not valid enum value: " + input);
    }

    @Override
    public @NonNull List<String> getSuggestion(@NonNull Class<?> argumentClass, @NonNull String input) {
        final Class<? extends Enum> enumClass = (Class<? extends Enum>) argumentClass;

        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
