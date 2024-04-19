package cc.dreamcode.command;

import lombok.NonNull;

public interface CommandScheduler {

    default void sync(@NonNull Runnable runnable) {
        runnable.run();
    }

    void async(@NonNull Runnable runnable);
}
