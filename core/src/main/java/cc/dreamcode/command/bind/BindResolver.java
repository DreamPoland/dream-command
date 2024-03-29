package cc.dreamcode.command.bind;

import cc.dreamcode.command.DreamSender;
import lombok.NonNull;

public interface BindResolver<T> {

    boolean isAssignableFrom(@NonNull Class<?> type);

    @NonNull T resolveBind(@NonNull DreamSender<?> sender);
}
