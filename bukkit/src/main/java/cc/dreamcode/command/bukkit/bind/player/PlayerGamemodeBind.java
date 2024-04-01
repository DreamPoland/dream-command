package cc.dreamcode.command.bukkit.bind.player;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.handler.exception.InvalidSenderException;
import lombok.NonNull;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Collections;

public class PlayerGamemodeBind implements BindResolver<GameMode> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return GameMode.class.isAssignableFrom(type);
    }

    @Override
    public @NonNull GameMode resolveBind(@NonNull DreamSender<?> sender) {

        if (!(sender.getHandler() instanceof Player)) {
            throw new InvalidSenderException(Collections.singletonList(DreamSender.Type.CLIENT), "Sender type is unacceptable (" + sender.getType() + ")");
        }

        final Player player = (Player) sender.getHandler();
        return player.getGameMode();
    }
}
