package cc.dreamcode.command;

import lombok.NonNull;

import java.util.List;

public interface DreamCommand<C, P> {
    void content(@NonNull C sender, @NonNull String[] args);

    List<String> tab(@NonNull P player, @NonNull String[] args);
}
