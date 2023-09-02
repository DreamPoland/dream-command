package cc.dreamcode.commandtest.bind.type;

import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.commandtest.sender.TestSender;
import lombok.NonNull;

public class TestCommandSenderBind implements BindResolver<TestSender> {
    @Override
    public @NonNull TestSender resolveBind(@NonNull DreamSender<?> sender) {
        return new TestSender();
    }
}
