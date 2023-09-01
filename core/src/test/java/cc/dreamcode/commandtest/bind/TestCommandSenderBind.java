package cc.dreamcode.commandtest.bind;

import cc.dreamcode.command.DreamCommandSender;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.commandtest.TestCommandSender;
import lombok.NonNull;

public class TestCommandSenderBind implements BindResolver<TestCommandSender> {
    @Override
    public @NonNull TestCommandSender resolveBind(@NonNull DreamCommandSender<?> sender) {
        return new TestCommandSender();
    }
}
