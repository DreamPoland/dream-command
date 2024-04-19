package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandScheduler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class BukkitCommandScheduler implements CommandScheduler {

    private final Plugin plugin;

    @Override
    public void async(@NonNull Runnable runnable) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }
}
