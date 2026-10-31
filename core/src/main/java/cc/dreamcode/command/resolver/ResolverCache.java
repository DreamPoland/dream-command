package cc.dreamcode.command.resolver;

import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ResolverCache {

    private final List<ObjectTransformer<?>> objectTransformers = new CopyOnWriteArrayList<>();
    private final List<ArrayTransformer<?>> arrayTransformers = new CopyOnWriteArrayList<>();
    private final Map<Class<?>, List<Class<?>>> assignableClasses = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Optional<ObjectTransformer<T>> get(@NonNull Class<T> type) {
        return this.objectTransformers.stream()
                .filter(objectTransformer -> {

                    final List<Class<?>> classList = this.assignableClasses.get(objectTransformer.getGeneric());
                    if (classList == null) {
                        return objectTransformer.isAssignableFrom(type);
                    }

                    return objectTransformer.isAssignableFrom(type) || classList.contains(type);
                })
                .map(objectTransformer -> (ObjectTransformer<T>) objectTransformer)
                .findAny();
    }

    public Optional<ArrayTransformer<?>> getArray(@NonNull Class<?> type) {
        return this.arrayTransformers.stream()
                .filter(arrayTransformer -> {

                    final List<Class<?>> classList = this.assignableClasses.get(arrayTransformer.getGeneric());
                    if (classList == null) {
                        return arrayTransformer.isAssignableFrom(type);
                    }

                    return arrayTransformer.isAssignableFrom(type) || classList.contains(type);
                })
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

    public ResolverCache addAssignableClass(@NonNull Class<?> from, @NonNull Class<?> to) {
        this.assignableClasses.computeIfAbsent(from, k -> new CopyOnWriteArrayList<>()).add(to);
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

    public ResolverCache removeAssignableClass(@NonNull Class<?> from, @NonNull Class<?> to) {
        final List<Class<?>> classList = this.assignableClasses.get(from);
        if (classList != null) {
            classList.remove(to);
        }
        return this;
    }
}
