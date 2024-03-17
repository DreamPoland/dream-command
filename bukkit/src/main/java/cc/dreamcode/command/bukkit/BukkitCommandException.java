package cc.dreamcode.command.bukkit;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

@Getter
public final class BukkitCommandException extends RuntimeException {

    private final Consumer<CommandSender> handler;

    public BukkitCommandException(@NonNull Consumer<CommandSender> notice) {
        this.handler = notice;
    }
}
