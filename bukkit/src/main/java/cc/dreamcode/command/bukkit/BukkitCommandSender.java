package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.DreamCommandSender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class BukkitCommandSender implements DreamCommandSender<CommandSender> {

    private final CommandSender sender;

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
