package cc.dreamcode.command.bind;

import lombok.NonNull;

public interface BindRegistry {
    void register(@NonNull BindManager bindManager);
}
