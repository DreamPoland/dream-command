package cc.dreamcode.command.handler;

import cc.dreamcode.command.CommandInput;
import cc.dreamcode.command.CommandMeta;
import cc.dreamcode.command.DreamSender;
import lombok.NonNull;

import java.util.Optional;

public interface InvalidUsageHandler {

    void handle(@NonNull DreamSender<?> dreamSender, @NonNull Optional<CommandMeta> commandMeta, @NonNull CommandInput commandInput);
}
