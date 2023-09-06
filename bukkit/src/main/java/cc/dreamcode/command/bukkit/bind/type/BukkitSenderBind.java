package cc.dreamcode.command.bukkit.bind.type;

import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.bukkit.sender.BukkitSender;
import cc.dreamcode.command.sender.DreamSender;
import lombok.NonNull;

public class BukkitSenderBind implements BindResolver<BukkitSender> {
    @Override
    public @NonNull Class<BukkitSender> getClassType() {
        return BukkitSender.class;
    }

    @Override
    public @NonNull BukkitSender resolveBind(@NonNull DreamSender<?> sender) {
        return (BukkitSender) sender;
    }
}
