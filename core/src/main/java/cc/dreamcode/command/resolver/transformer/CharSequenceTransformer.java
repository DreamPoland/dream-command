package cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.command.resolver.ObjectTransformer;
import lombok.NonNull;

public class CharSequenceTransformer implements ObjectTransformer<CharSequence> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return CharSequence.class.isAssignableFrom(type);
    }

    @Override
    public CharSequence transform(@NonNull String input) {
        return input;
    }
}
