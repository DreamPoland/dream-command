package cc.dreamcode.command.bungee;

import cc.dreamcode.command.bungee.bind.CommandSenderBind;
import cc.dreamcode.command.bungee.bind.ProxiedPlayerBind;
import cc.dreamcode.command.bungee.bind.player.PlayerServerBind;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BungeeBindTest {

    @Test
    void proxiedPlayerBindShouldMatchProxiedPlayer() {
        ProxiedPlayerBind bind = new ProxiedPlayerBind();

        assertTrue(bind.isAssignableFrom(ProxiedPlayer.class));
        assertFalse(bind.isAssignableFrom(CommandSender.class));
    }

    @Test
    void commandSenderBindShouldMatchAllSenders() {
        CommandSenderBind bind = new CommandSenderBind();

        assertTrue(bind.isAssignableFrom(CommandSender.class));
        assertTrue(bind.isAssignableFrom(ProxiedPlayer.class));
    }

    @Test
    void playerServerBindShouldMatchServer() {
        PlayerServerBind bind = new PlayerServerBind();

        assertTrue(bind.isAssignableFrom(Server.class));
    }
}
