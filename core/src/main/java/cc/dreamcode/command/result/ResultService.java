package cc.dreamcode.command.result;

import cc.dreamcode.command.DreamSender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ResultService {

    private final ResultCache resultCache;

    public void resolveResult(@NonNull DreamSender<?> sender, @NonNull Class<?> type, @NonNull Object object) {

        final Optional<ResultResolver> optionalResultResolver = this.resultCache.get(type);
        if (!optionalResultResolver.isPresent()) {
            throw new RuntimeException("Cannot resolve method result, missing resolver: " + type);
        }

        final ResultResolver resultResolver = optionalResultResolver.get();
        resultResolver.resolveResult(sender, type, object);
    }
}
