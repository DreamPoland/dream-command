package cc.dreamcode.command;

import lombok.NonNull;

import java.util.function.Consumer;

public interface DreamCommandProvider<C, P> {

    void addCommand(@NonNull C c);

    void setNoPermissionHandler(@NonNull Consumer<P> p);

    void setNotPlayerHandler(@NonNull Consumer<P> p);
}
