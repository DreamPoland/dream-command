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

    public <T> void registerExtension(@NonNull Class<T> argumentClass, @NonNull ExtensionResolver<T> extensionResolver) {
        this.extensionResolverMap.put(argumentClass, extensionResolver);
    }

    public void registerExtension(@NonNull ExtensionRegistry registry) {
        registry.register(this);
    }

    public void unregisterExtension(@NonNull Class<?> argumentClass) {
        this.extensionResolverMap.remove(argumentClass);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> resolveObject(@NonNull Class<T> argumentClass, @NonNull String input) {
        final Map<Class<?>, ExtensionResolver<?>> argumentHandlerMap = this.getExtensionMap();

        return (Optional<T>) argumentHandlerMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAssignableFrom(argumentClass)) // TODO: 26.08.2023 maybe bad practice
                .map(Map.Entry::getValue)
                .map(extensionResolver -> extensionResolver.resolveArgument(input))
                .findAny();
    }
}
