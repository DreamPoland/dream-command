package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.DreamSender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class BukkitSender implements DreamSender<CommandSender> {

    private final CommandSender commandSender;

    @Override
    public Type getType() {

        if (this.commandSender instanceof Player) {
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
