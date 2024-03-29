package cc.dreamcode.command.bukkit.bind;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.bukkit.BukkitSender;
import lombok.NonNull;

public class BukkitSenderBind implements BindResolver<BukkitSender> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return BukkitSender.class.isAssignableFrom(type);
    }

    @Override
    public @NonNull BukkitSender resolveBind(@NonNull DreamSender<?> sender) {
        return (BukkitSender) sender;
    }
}
