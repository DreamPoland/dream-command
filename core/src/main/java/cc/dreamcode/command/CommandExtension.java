package cc.dreamcode.command;

import lombok.NonNull;

public interface CommandExtension {

    void register(@NonNull CommandProvider commandProvider);
}
