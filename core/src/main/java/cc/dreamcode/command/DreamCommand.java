package cc.dreamcode.command;

import lombok.NonNull;

public interface DreamCommand {

    DreamCommandRegistry getCommandRegistry();

    void registerCommand(@NonNull DreamCommandExecutor executor);
}
