package cc.dreamcode.command.bungee.sender;

import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.command.sender.SenderType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@RequiredArgsConstructor
public class BungeeSender implements DreamSender<CommandSender> {

    private final CommandSender sender;

    @Override
    public SenderType getSenderType() {
        return this.sender instanceof ProxiedPlayer ? SenderType.PLAYER : SenderType.CONSOLE;
    }

    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public void sendMessage(@NonNull String text) {
        this.sender.sendMessage(text);
    }

    @Override
    public boolean hasPermission(@NonNull String permission) {
        return this.sender.hasPermission(permission);
    }

    @Override
    public CommandSender getConsumer() {
        return this.sender;
    }
}
