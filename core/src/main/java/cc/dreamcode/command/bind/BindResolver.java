package cc.dreamcode.command.bind;

import cc.dreamcode.command.DreamCommandSender;
import lombok.NonNull;

public interface BindResolver<A> {
    @NonNull A resolveBind(@NonNull DreamCommandSender<?> sender);
}
