package cc.dreamcode.command.resolver;

import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ResolverCache {

    private final List<ObjectTransformer<?>> objectTransformers = new ArrayList<>();
    private final List<ArrayTransformer<?>> arrayTransformers = new ArrayList<>();
    private final Map<Class<?>, List<Class<?>>> assignableClasses = new HashMap<>(); // Integer.class int.class

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

    @SuppressWarnings("unchecked")
    public <T> Optional<ArrayTransformer<T>> getArray(@NonNull Class<T> type) {
        return this.arrayTransformers.stream()
                .filter(arrayTransformer -> {

                    final List<Class<?>> classList = this.assignableClasses.get(arrayTransformer.getGeneric());
                    if (classList == null) {
                        return arrayTransformer.isAssignableFrom(type);
                    }

                    return arrayTransformer.isAssignableFrom(type) || classList.contains(type);
                })
                .map(arrayTransformer -> (ArrayTransformer<T>) arrayTransformer)
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

        final List<Class<?>> classList = this.assignableClasses.get(from);
        if (classList == null) {
            this.assignableClasses.put(from, ListBuilder.of(to));
            return this;
        }

        classList.add(to);
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
        if (classList == null) {
            return this;
        }

        classList.remove(to);
        return this;
    }
}
