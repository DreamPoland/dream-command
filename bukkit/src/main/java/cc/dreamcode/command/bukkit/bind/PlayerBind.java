package cc.dreamcode.command.bukkit.bind;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class PlayerBind implements BindResolver<Player> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Player.class.isAssignableFrom(type);
    }

    @Override
    public @NonNull Player resolveBind(@NonNull DreamSender<?> sender) {
        return (Player) sender.getHandler();
    }
}
