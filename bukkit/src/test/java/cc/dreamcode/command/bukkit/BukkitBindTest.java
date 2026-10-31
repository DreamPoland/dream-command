package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.bukkit.bind.CommandSenderBind;
import cc.dreamcode.command.bukkit.bind.ConsoleCommandSenderBind;
import cc.dreamcode.command.bukkit.bind.PlayerBind;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BukkitBindTest {

    @Test
    void consoleCommandSenderBindShouldOnlyMatchConsole() {
        ConsoleCommandSenderBind bind = new ConsoleCommandSenderBind();

        assertTrue(bind.isAssignableFrom(ConsoleCommandSender.class));
        assertFalse(bind.isAssignableFrom(CommandSender.class), "ConsoleCommandSenderBind must not claim to match generic CommandSender");
        assertFalse(bind.isAssignableFrom(Player.class), "ConsoleCommandSenderBind must not match Player");
    }

    @Test
    void playerBindShouldOnlyMatchPlayer() {
        PlayerBind bind = new PlayerBind();

        assertTrue(bind.isAssignableFrom(Player.class));
        assertFalse(bind.isAssignableFrom(ConsoleCommandSender.class));
        assertFalse(bind.isAssignableFrom(CommandSender.class));
    }

    @Test
    void commandSenderBindShouldMatchAllSenders() {
        CommandSenderBind bind = new CommandSenderBind();

        assertTrue(bind.isAssignableFrom(CommandSender.class));
        assertTrue(bind.isAssignableFrom(Player.class));
        assertTrue(bind.isAssignableFrom(ConsoleCommandSender.class));
    }
}
