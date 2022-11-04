package cc.dreamcode.command;

import lombok.NonNull;

import java.util.List;

public interface CommandPlatform<C, P> {
    void handle(@NonNull C sender, @NonNull String[] args);

    List<String> tab(@NonNull P player, @NonNull String[] args);
}
