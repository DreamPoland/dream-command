package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.DreamArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BukkitArgument extends DreamArgument<CommandSender, Player> {
    public BukkitArgument(String name, int arg) {
        super(name, arg);
    }
}
