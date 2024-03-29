package cc.dreamcode.command.handler;

import cc.dreamcode.command.CommandSender;
import lombok.NonNull;

import java.util.List;

public interface InvalidSenderHandler {

    void handle(@NonNull CommandSender<?> commandSender, @NonNull List<CommandSender.Type> requireType);
}
