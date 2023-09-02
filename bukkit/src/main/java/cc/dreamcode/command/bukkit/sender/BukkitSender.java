package cc.dreamcode.command.bukkit.sender;

import cc.dreamcode.command.sender.SenderType;
import cc.dreamcode.command.sender.DreamSender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class BukkitSender implements DreamSender<CommandSender> {

    private final CommandSender sender;

    @Override
    public SenderType getSenderType() {
        return this.sender instanceof Player ? SenderType.PLAYER : SenderType.CONSOLE;
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
