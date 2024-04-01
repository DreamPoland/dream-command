package cc.dreamcode.command.bukkit.bind;

import cc.dreamcode.command.DreamSender;
import cc.dreamcode.command.bind.BindResolver;
import lombok.NonNull;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public class ServerBind implements BindResolver<Server> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<?> type) {
        return Server.class.isAssignableFrom(type);
    }

    @Override
    public @NonNull Server resolveBind(@NonNull DreamSender<?> sender) {
        final CommandSender commandSender = (CommandSender) sender.getHandler();
        return commandSender.getServer();
    }
}
