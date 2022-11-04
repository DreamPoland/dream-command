package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.ArgumentHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BukkitArgumentHandler extends ArgumentHandler<CommandSender, Player> {
    public BukkitArgumentHandler(String name, int arg) {
        super(name, arg);
    }
}
