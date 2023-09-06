package cc.dreamcode.command.bungee.bind.type;

import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.bungee.sender.BungeeSender;
import cc.dreamcode.command.sender.DreamSender;
import lombok.NonNull;

public class BungeeSenderBind implements BindResolver<BungeeSender> {
    @Override
    public @NonNull Class<BungeeSender> getClassType() {
        return BungeeSender.class;
    }

    @Override
    public @NonNull BungeeSender resolveBind(@NonNull DreamSender<?> sender) {
        return (BungeeSender) sender;
    }
}
