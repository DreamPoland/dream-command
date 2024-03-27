package cc.dreamcode.command;

import cc.dreamcode.command.bind.BindResolver;
import lombok.NonNull;

public class TestSenderBind implements BindResolver<TestSender> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return TestSender.class.isAssignableFrom(type);
    }

    @Override
    public @NonNull TestSender resolveBind(@NonNull CommandSender<?> sender) {
        return (TestSender) sender;
    }
}
