package cc.dreamcode.command.resolver;

import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ResolverService {

    private final ResolverCache resolverCache;

    public boolean support(@NonNull Class<?> expectingClass, @NonNull String input) {
        return this.resolve(expectingClass, input).isPresent();
    }

    public boolean supportArray(@NonNull Class<?> expectingClass, @NonNull Object[] input) {
        return this.resolveArray(expectingClass, input).isPresent();
    }

    public <T> Optional<T> resolve(@NonNull Class<T> expectingClass, @NonNull String input) {

        final ObjectTransformer<T> objectTransformer = this.resolverCache.get(expectingClass)
                .orElseThrow(() -> new RuntimeException("Cannot find resolver for " + expectingClass));

        return objectTransformer.transform(expectingClass, input);
    }

    public <T> Optional<T[]> resolveArray(@NonNull Class<T> expectingClass, @NonNull Object[] input) {

        final ArrayTransformer<T> arrayTransformerTransformer = this.resolverCache.getArray(expectingClass)
                .orElseThrow(() -> new RuntimeException("Cannot find array-resolver for " + expectingClass));

        return arrayTransformerTransformer.transform(expectingClass, input);
    }
}
