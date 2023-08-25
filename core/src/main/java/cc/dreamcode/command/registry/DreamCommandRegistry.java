package cc.dreamcode.command.registry;

import cc.dreamcode.command.DreamCommandContext;
import cc.dreamcode.command.DreamCommandExecutor;
import lombok.NonNull;

public interface DreamCommandRegistry {

    String getBaseName();

    void registerCommand(@NonNull DreamCommandContext context, @NonNull DreamCommandExecutor executor);

    void disposeCommand(@NonNull DreamCommandContext context);

    void disposeAll();
}
