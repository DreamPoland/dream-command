package cc.dreamcode.command.resolver;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ObjectResolverCache {

    private final List<ObjectTransformer<?>> objectTransformers = new ArrayList<>();

    public Optional<ObjectTransformer<?>> get(@NonNull Class<?> type) {
        return this.objectTransformers.stream()
                .filter(objectTransformer -> objectTransformer.isAssignableFrom(type))
                .findAny();
    }

    public ObjectResolverCache add(@NonNull ObjectTransformer<?> objectTransformer) {
        this.objectTransformers.add(objectTransformer);
        return this;
    }

    public ObjectResolverCache remove(@NonNull Class<?> type) {
        this.objectTransformers.removeIf(objectTransformer -> objectTransformer.isAssignableFrom(type));
        return this;
    }
}
