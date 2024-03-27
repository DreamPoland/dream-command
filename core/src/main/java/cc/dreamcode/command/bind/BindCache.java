package cc.dreamcode.command.bind;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BindCache {

    private final List<BindResolver<?>> bindResolvers = new ArrayList<>();

    public Optional<BindResolver<?>> get(@NonNull Class<?> type) {
        return this.bindResolvers.stream()
                .filter(bindResolver -> bindResolver.isAssignableFrom(type))
                .findAny();
    }

    public void registerBind(@NonNull BindResolver<?> bindResolver) {
        this.bindResolvers.add(bindResolver);
    }

    public void unregisterBind(@NonNull Class<?> bindClass) {
        this.bindResolvers.removeIf(bindResolver -> bindResolver.isAssignableFrom(bindClass));
    }
}
