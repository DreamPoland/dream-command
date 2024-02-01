package cc.dreamcode.command;

import lombok.NonNull;

public interface DreamCommandProvider<P> {

    void addCommand(@NonNull P p);

    void setRequiredPermissionMessage(@NonNull String notice);

    void setRequiredPlayerMessage(@NonNull String notice);
}
