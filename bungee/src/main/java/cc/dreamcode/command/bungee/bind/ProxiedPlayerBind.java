package cc.dreamcode.command.bungee.bind;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ProxiedPlayerBind implements BindResolver<ProxiedPlayer> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return ProxiedPlayer.class.isAssignableFrom(type);
    }

    @Override
    public @NonNull ProxiedPlayer resolveBind(@NonNull DreamSender<?> sender) {
        return (ProxiedPlayer) sender.getHandler();
    }
}
