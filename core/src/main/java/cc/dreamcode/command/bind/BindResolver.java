package cc.dreamcode.command.bind;

import cc.dreamcode.command.sender.DreamSender;
import lombok.NonNull;

public interface BindResolver<A> {
    @NonNull A resolveBind(@NonNull DreamSender<?> sender);
}
