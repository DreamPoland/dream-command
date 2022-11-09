package cc.dreamcode.command.bungee;


import cc.dreamcode.command.DreamArgument;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class BungeeArgument extends DreamArgument<CommandSender, ProxiedPlayer> {
    public BungeeArgument(String name, int arg) {
        super(name, arg);
    }
}
