package cc.dreamcode.command.bungee.bind;

import cc.dreamcode.command.bind.BindManager;
import cc.dreamcode.command.bind.BindRegistry;
import cc.dreamcode.command.bungee.bind.type.BungeeSenderBind;
import lombok.NonNull;

public class BungeeBindRegistry implements BindRegistry {
    @Override
    public void register(@NonNull BindManager bindManager) {
        bindManager.registerBind(new BungeeSenderBind());
    }
}
