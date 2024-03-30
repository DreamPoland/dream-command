package cc.dreamcode.command.bungee.bind;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.bungee.BungeeSender;
import lombok.NonNull;

public class BungeeSenderBind implements BindResolver<BungeeSender> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return BungeeSender.class.isAssignableFrom(type);
    }

    @Override
    public @NonNull BungeeSender resolveBind(@NonNull DreamSender<?> sender) {
        return (BungeeSender) sender;
    }
}
