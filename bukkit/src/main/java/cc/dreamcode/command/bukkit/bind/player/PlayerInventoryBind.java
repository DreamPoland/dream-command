package cc.dreamcode.command.bukkit.bind.player;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.handler.exception.InvalidSenderException;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;

public class PlayerInventoryBind implements BindResolver<Inventory> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Inventory.class.isAssignableFrom(type);
    }

    @Override
    public @NonNull Inventory resolveBind(@NonNull DreamSender<?> sender) {

        if (!(sender.getHandler() instanceof Player)) {
            throw new InvalidSenderException(Collections.singletonList(DreamSender.Type.CLIENT), "Sender type is unacceptable (" + sender.getType() + ")");
        }

        final Player player = (Player) sender.getHandler();
        return player.getInventory();
    }
}
