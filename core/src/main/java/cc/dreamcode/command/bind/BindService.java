package cc.dreamcode.command.bind;

import cc.dreamcode.command.DreamSender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class BindService {

    private final BindCache bindCache;

    public Optional<?> resolveBind(@NonNull Class<?> bindClass, @NonNull DreamSender<?> sender) {

        final Optional<BindResolver<?>> optionalBindResolver = this.bindCache.get(bindClass);
        if (!optionalBindResolver.isPresent()) {
            return Optional.empty();
        }

        final BindResolver<?> bindResolver = optionalBindResolver.get();
        return Optional.of(bindResolver.resolveBind(sender));
    }
}
