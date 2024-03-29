package cc.dreamcode.command.resolver;

import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResolverCache {

    private final List<ObjectTransformer<?>> objectTransformers = new ArrayList<>();
    private final List<ArrayTransformer<?>> arrayTransformers = new ArrayList<>();

    public Optional<ObjectTransformer<?>> get(@NonNull Class<?> type) {
        return this.objectTransformers.stream()
                .filter(objectTransformer -> objectTransformer.isAssignableFrom(type))
                .findAny();
    }

    public Optional<ArrayTransformer<?>> getArray(@NonNull Class<?> type) {
        return this.arrayTransformers.stream()
                .filter(arrayTransformer -> arrayTransformer.isAssignableFrom(type))
                .findAny();
    }

    public List<ObjectTransformer<?>> getObjectTransformers() {
        return new ArrayList<>(this.objectTransformers);
    }

    public ResolverCache add(@NonNull ObjectTransformer<?> objectTransformer) {
        this.objectTransformers.add(objectTransformer);
        return this;
    }

    public ResolverCache add(@NonNull ArrayTransformer<?> arrayTransformer) {
        this.arrayTransformers.add(arrayTransformer);
        return this;
    }

    public ResolverCache remove(@NonNull Class<?> type) {
        this.objectTransformers.removeIf(objectTransformer -> objectTransformer.isAssignableFrom(type));
        return this;
    }

    public ResolverCache removeArray(@NonNull Class<?> type) {
        this.arrayTransformers.removeIf(arrayTransformer -> arrayTransformer.isAssignableFrom(type));
        return this;
    }
}
