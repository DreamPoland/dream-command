package cc.dreamcode.command.bukkit.bind;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

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
