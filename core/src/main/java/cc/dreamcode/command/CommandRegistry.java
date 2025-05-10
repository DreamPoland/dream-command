package cc.dreamcode.command;

import lombok.NonNull;

public interface CommandRegistry {

    void register(@NonNull CommandEntry commandEntry, @NonNull CommandMeta commandMeta);

    void unregister(@NonNull CommandEntry commandEntry);
}
