package cc.dreamcode.command.resolver;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ResolverService {

    private final ResolverCache resolverCache;

    public boolean support(@NonNull Class<?> expectingClass, @NonNull String input) {

        final Optional<ObjectTransformer<?>> optionalObjectTransformer = this.resolverCache.get(expectingClass);
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

        final ObjectTransformer<?> objectTransformer = this.resolverCache.get(expectingClass)
                .orElseThrow(() -> new RuntimeException("Cannot find resolver for class " + expectingClass));

        return objectTransformer.transform(expectingClass, input);
    }
}
