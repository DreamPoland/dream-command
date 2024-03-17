package cc.dreamcode.command.bungee;

import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;

import java.util.function.Consumer;

@Getter
public final class BungeeCommandException extends RuntimeException {

    private final Consumer<CommandSender> handler;

    public BungeeCommandException(@NonNull Consumer<CommandSender> notice) {
        this.handler = notice;
    }
}
