package cc.dreamcode.command.result;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ResultCache {

    private final List<ResultResolver> resultResolvers = new CopyOnWriteArrayList<>();

    public Optional<ResultResolver> get(@NonNull Class<?> type) {
        return this.resultResolvers.stream()
                .filter(resultResolver -> resultResolver.isAssignableFrom(type))
                .findAny();
    }

    public void registerResult(@NonNull ResultResolver resultResolver) {
        this.resultResolvers.add(resultResolver);
    }

    public void unregisterResult(@NonNull Class<?> resultResolverClass) {
        this.resultResolvers.removeIf(resultResolver -> resultResolver.isAssignableFrom(resultResolverClass));
    }
}
