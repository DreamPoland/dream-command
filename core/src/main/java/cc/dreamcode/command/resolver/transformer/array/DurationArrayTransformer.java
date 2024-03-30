package cc.dreamcode.command.resolver.transformer.array;

import lombok.NonNull;

import java.time.Duration;
import java.util.Optional;

public class DurationArrayTransformer implements ArrayTransformer<Duration> {
    @Override
    public Class<?> getGeneric() {
        return Duration[].class;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Duration[].class.isAssignableFrom(type);
    }

    @Override
    public Optional<Duration[]> transform(@NonNull Class<?> type, @NonNull Object[] objects) {

        final Duration[] array = new Duration[objects.length];
        for (int index = 0; index < objects.length; index++) {
            array[index] = (Duration) objects[index];
        }

        return Optional.of(array);
    }
}
