package cc.dreamcode.command;

import lombok.NonNull;

public interface DreamCommandProvider<P, N> {

    void addCommand(@NonNull Class<P> pClass);

    void addCommand(@NonNull P p);

    void setRequiredPermissionMessage(@NonNull N notice);

    void setRequiredPlayerMessage(@NonNull N notice);
}
