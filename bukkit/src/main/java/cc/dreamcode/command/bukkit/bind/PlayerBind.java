package cc.dreamcode.command.bukkit.bind;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.handler.exception.InvalidSenderException;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.Collections;

public class PlayerBind implements BindResolver<Player> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Player.class.isAssignableFrom(type);
    }

    @Override
    public @NonNull Player resolveBind(@NonNull DreamSender<?> sender) {

        if (!(sender.getHandler() instanceof Player)) {
            throw new InvalidSenderException(Collections.singletonList(DreamSender.Type.CLIENT), "Sender type is unacceptable (" + sender.getType() + ")");
        }

        return (Player) sender.getHandler();
    }
}
