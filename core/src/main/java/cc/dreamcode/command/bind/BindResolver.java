package cc.dreamcode.command.bind;

import cc.dreamcode.command.CommandSender;
import lombok.NonNull;

public interface BindResolver<T> {

    boolean isAssignableFrom(@NonNull Class<?> type);

    @NonNull T resolveBind(@NonNull CommandSender<?> sender);
}
