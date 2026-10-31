package cc.dreamcode.command.bind;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class BindCache {

    private final List<BindResolver<?>> bindResolvers = new CopyOnWriteArrayList<>();

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
