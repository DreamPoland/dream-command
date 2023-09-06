package cc.dreamcode.command.bukkit.bind;

import cc.dreamcode.command.bind.BindManager;
import cc.dreamcode.command.bind.BindRegistry;
import cc.dreamcode.command.bukkit.bind.type.BukkitSenderBind;
import lombok.NonNull;

public class BukkitBindRegistry implements BindRegistry {
    @Override
    public void register(@NonNull BindManager bindManager) {
        bindManager.registerBind(new BukkitSenderBind());
    }
}
