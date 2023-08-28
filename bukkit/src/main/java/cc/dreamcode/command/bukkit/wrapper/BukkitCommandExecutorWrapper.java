package cc.dreamcode.command.bukkit.wrapper;

import cc.dreamcode.command.DreamCommandContext;
import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.extension.ExtensionManager;
import cc.dreamcode.command.path.CommandPath;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommandExecutorWrapper extends Command implements PluginIdentifiableCommand {

    private @Getter final Plugin plugin;
    private final DreamCommandContext context;
    private final DreamCommandExecutor executor;
    private final ExtensionManager extensions;

    public BukkitCommandExecutorWrapper(@NonNull Plugin plugin, @NonNull DreamCommandContext context, @NonNull DreamCommandExecutor executor, @NonNull ExtensionManager extensions) {
        super(context.getLabel());

        this.plugin = plugin;
        this.context = context;
        this.executor = executor;
        this.extensions = extensions;

        this.setDescription(context.getDescription());
        this.setAliases(context.getAliases());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        final CommandPath commandPath = new CommandPath(this.context.getLabel(), args);
        return this.executor.invokeMethod(this.extensions, commandPath);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return new ArrayList<>(); // TODO: 28.08.2023
    }
}
