package cc.dreamcode.command.bungee.bind.player;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.handler.exception.InvalidSenderException;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import java.util.Collections;

public class PlayerServerBind implements BindResolver<Server> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Server.class.isAssignableFrom(type);
    }

    @Override
    public @NonNull Server resolveBind(@NonNull DreamSender<?> sender) {

        if (!(sender.getHandler() instanceof ProxiedPlayer)) {
            throw new InvalidSenderException(Collections.singletonList(DreamSender.Type.CLIENT), "Sender type is unacceptable (" + sender.getType() + ")");
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender.getHandler();
        return player.getServer();
    }
}
