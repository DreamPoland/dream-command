package cc.dreamcode.command.result;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResultCache {

    private final List<ResultResolver> resultResolvers = new ArrayList<>();

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
