package cc.dreamcode.command;

import lombok.NonNull;

public interface DreamCommandRegistry {

    void registerCommand(@NonNull DreamCommandExecutor executor);

    void registerCommand(@NonNull DreamCommandContext context, @NonNull DreamCommandExecutor executor);

    void disposeCommand(@NonNull DreamCommandContext context);

    void disposeAll();
}
