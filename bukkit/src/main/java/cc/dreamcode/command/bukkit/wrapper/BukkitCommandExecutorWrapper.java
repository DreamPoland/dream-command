package cc.dreamcode.command.bukkit.wrapper;

import cc.dreamcode.command.DreamCommandContext;
import cc.dreamcode.command.DreamCommandExecutor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

@Getter
public class BukkitCommandExecutorWrapper extends Command implements PluginIdentifiableCommand {

    private final Plugin plugin;
    private final DreamCommandContext context;
    private final DreamCommandExecutor executor;

    public BukkitCommandExecutorWrapper(@NonNull Plugin plugin, @NonNull DreamCommandContext context, @NonNull DreamCommandExecutor executor) {
        super(context.getName());

        this.plugin = plugin;
        this.context = context;
        this.executor = executor;

        this.setDescription(context.getDescription());
        this.setAliases(context.getAliases());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return false;
    }
}
