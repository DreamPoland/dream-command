package cc.dreamcode.commandtest.bind;

import cc.dreamcode.command.bind.BindManager;
import cc.dreamcode.command.bind.BindRegistry;
import cc.dreamcode.commandtest.TestCommandSender;
import lombok.NonNull;

public class TestCommandBindRegistry implements BindRegistry {
    @Override
    public void register(@NonNull BindManager bindManager) {
        bindManager.registerBind(new TestCommandSenderBind(), TestCommandSender.class);
    }
}
