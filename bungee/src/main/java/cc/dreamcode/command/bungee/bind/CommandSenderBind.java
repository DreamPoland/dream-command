package cc.dreamcode.command.bungee.bind;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;

public class CommandSenderBind implements BindResolver<CommandSender> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return CommandSender.class.isAssignableFrom(type);
    }

    @Override
    public @NonNull CommandSender resolveBind(@NonNull DreamSender<?> sender) {
        return (CommandSender) sender.getHandler();
    }
}
