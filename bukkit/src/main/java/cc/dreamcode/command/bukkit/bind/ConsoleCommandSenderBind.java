package cc.dreamcode.command.bukkit.bind;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.command.handler.exception.InvalidSenderException;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Collections;

public class ConsoleCommandSenderBind implements BindResolver<ConsoleCommandSender> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return CommandSender.class.isAssignableFrom(type);
    }

    @Override
    public @NonNull ConsoleCommandSender resolveBind(@NonNull DreamSender<?> sender) {

        if (!(sender.getHandler() instanceof ConsoleCommandSender)) {
            throw new InvalidSenderException(Collections.singletonList(DreamSender.Type.CONSOLE), "Sender type is unacceptable (" + sender.getType() + ")");
        }

        return (ConsoleCommandSender) sender.getHandler();
    }
}
