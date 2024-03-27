package cc.dreamcode.command.resolver;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ObjectResolverService {

    private final ObjectResolverCache objectResolverCache;

    public boolean support(@NonNull Class<?> expectingClass, @NonNull String input) {

        final Optional<ObjectTransformer<?>> optionalObjectTransformer = this.objectResolverCache.get(expectingClass);
        if (!optionalObjectTransformer.isPresent()) {
            return false;
        }

        final ObjectTransformer<?> objectTransformer = optionalObjectTransformer.get();
        if (!objectTransformer.isAssignableFrom(expectingClass)) {
            return false;
        }

        return objectTransformer.transform(expectingClass, input).isPresent();
    }

    public Optional<?> resolve(@NonNull Class<?> expectingClass, @NonNull String input) {

        final ObjectTransformer<?> objectTransformer = this.objectResolverCache.get(expectingClass)
                .orElseThrow(() -> new RuntimeException("Cannot find resolver for class " + expectingClass));

        return objectTransformer.transform(expectingClass, input);
    }
}
