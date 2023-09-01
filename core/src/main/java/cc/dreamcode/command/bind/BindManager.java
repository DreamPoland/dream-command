package cc.dreamcode.command.bind;

import cc.dreamcode.command.DreamCommandSender;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BindManager {

    private final Map<Class<?>, BindResolver<?>> bindResolverMap = new HashMap<>();

    public final <T> void registerBind(@NonNull BindResolver<T> bindResolver, @NonNull Class<T> bindClass) {
        this.bindResolverMap.put(bindClass, bindResolver);
    }

    public void registerBind(@NonNull BindRegistry bindRegistry) {
        bindRegistry.register(this);
    }

    public void unregisterBind(@NonNull Class<?> bindClass) {
        this.bindResolverMap.remove(bindClass);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> resolveBind(@NonNull Class<T> bindClass, @NonNull DreamCommandSender<?> sender) {
        for (Map.Entry<Class<?>, BindResolver<?>> entry : this.bindResolverMap.entrySet()) {
            if (!entry.getKey().isAssignableFrom(bindClass)) {
                continue;
            }

            final BindResolver<?> bindResolver = entry.getValue();
            return (Optional<T>) Optional.of(bindResolver.resolveBind(sender));
        }

        return Optional.empty();
    }
}
