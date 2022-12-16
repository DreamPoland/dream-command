package cc.dreamcode.command;

import lombok.NonNull;

public interface DreamCommandProvider<P, N> {

    void addCommand(@NonNull Class<P> pClass);

    void addCommand(@NonNull P p);

    void setNoPermissionMessage(@NonNull N notice);

    void setNoPlayerMessage(@NonNull N notice);
}
