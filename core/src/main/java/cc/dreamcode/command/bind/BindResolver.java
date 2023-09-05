package cc.dreamcode.command.bind;

import cc.dreamcode.command.sender.DreamSender;
import lombok.NonNull;

public interface BindResolver<A> {

    @NonNull Class<A> getClassType();

    @NonNull A resolveBind(@NonNull DreamSender<?> sender);
}
