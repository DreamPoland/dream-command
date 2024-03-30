package cc.dreamcode.command.bungee;

import cc.dreamcode.command.DreamSender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@RequiredArgsConstructor
public class BungeeSender implements DreamSender<CommandSender> {

    private final CommandSender commandSender;

    @Override
    public Type getType() {

        if (this.commandSender instanceof ProxiedPlayer) {
            return Type.CLIENT;
        }

        return Type.CONSOLE;
    }

    @Override
    public String getName() {
        return this.commandSender.getName();
    }

    @Override
    public boolean hasPermission(@NonNull String permission) {
        return this.commandSender.hasPermission(permission);
    }

    @Override
    public CommandSender getHandler() {
        return this.commandSender;
    }
}
