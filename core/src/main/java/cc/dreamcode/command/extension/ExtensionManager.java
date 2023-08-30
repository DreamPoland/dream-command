package cc.dreamcode.command.extension;

import lombok.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExtensionManager {

    private final Map<Class<?>, ExtensionResolver<?>> extensionResolverMap = new HashMap<>();

    public Map<Class<?>, ExtensionResolver<?>> getExtensionMap() {
        return Collections.unmodifiableMap(this.extensionResolverMap);
    }

    public final <T> void registerExtension(@NonNull ExtensionResolver<T> extensionResolver, @NonNull Class<T> argumentClass) {
        this.extensionResolverMap.put(argumentClass, extensionResolver);
    }

    public void registerExtension(@NonNull ExtensionRegistry registry) {
        registry.register(this);
    }

    public void unregisterExtension(@NonNull Class<?> argumentClass) {
        this.extensionResolverMap.remove(argumentClass);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> resolveObject(@NonNull Class<T> argumentClass, @NonNull String input) throws IllegalArgumentException {
        final Map<Class<?>, ExtensionResolver<?>> argumentHandlerMap = this.getExtensionMap();

        for (Map.Entry<Class<?>, ExtensionResolver<?>> entry : argumentHandlerMap.entrySet()) {
            if (!entry.getKey().isAssignableFrom(argumentClass)) {
                continue;
            }

            final ExtensionResolver<?> extensionResolver = entry.getValue();
            return (Optional<T>) Optional.of(extensionResolver.resolveArgument(input));
        }

        return Optional.empty();
    }
}
