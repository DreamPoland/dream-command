package cc.dreamcode.command.handler;

import cc.dreamcode.command.CommandInput;
import cc.dreamcode.command.CommandMeta;
import cc.dreamcode.command.CommandSender;
import lombok.NonNull;

import java.util.Optional;

public interface InvalidUsageHandler {

    void handle(@NonNull CommandSender<?> commandSender, @NonNull Optional<CommandMeta> commandMeta, @NonNull CommandInput commandInput);
}
