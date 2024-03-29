package cc.dreamcode.command;

import lombok.NonNull;

public interface CommandRegistry {
    void register(@NonNull CommandContext commandContext, @NonNull CommandMeta commandMeta);

    void unregister(@NonNull CommandContext commandContext);
}
