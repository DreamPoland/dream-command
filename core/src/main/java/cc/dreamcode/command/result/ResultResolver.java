package cc.dreamcode.command.result;

import cc.dreamcode.command.DreamSender;
import lombok.NonNull;

public interface ResultResolver {

    boolean isAssignableFrom(@NonNull Class<?> type);

    void resolveResult(@NonNull DreamSender<?> sender, @NonNull Class<?> type, @NonNull Object object);
}
