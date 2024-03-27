package cc.dreamcode.command.resolver;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObjectResolverService {

    private final ObjectResolverCache objectResolverCache;

    public Object resolve(@NonNull Class<?> expectingClass, @NonNull String input) {

        final ObjectTransformer<?> objectTransformer = this.objectResolverCache.get(expectingClass)
                .orElseThrow(() -> new RuntimeException("Cannot resolve class " + expectingClass));

        return objectTransformer.transform(input);
    }
}
