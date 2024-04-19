package cc.dreamcode.command.bungee;

import cc.dreamcode.command.CommandScheduler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.plugin.Plugin;

@RequiredArgsConstructor
public class BungeeCommandScheduler implements CommandScheduler {

    private final Plugin plugin;

    @Override
    public void async(@NonNull Runnable runnable) {
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, runnable);
    }
}
