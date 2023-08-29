package cc.dreamcode.command;

import cc.dreamcode.command.context.CommandContext;
import lombok.NonNull;

public interface DreamCommandRegistry {

    void registerCommand(@NonNull DreamCommandExecutor executor);

    void registerCommand(@NonNull CommandContext context, @NonNull DreamCommandExecutor executor);

    void disposeCommand(@NonNull CommandContext context);

    void disposeAll();
}
