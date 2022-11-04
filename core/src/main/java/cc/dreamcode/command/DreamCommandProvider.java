package cc.dreamcode.command;

import lombok.NonNull;

public interface DreamCommandProvider<P> {

    void addCommand(@NonNull Class<P> pClass);

    void addCommand(@NonNull P p);
}
