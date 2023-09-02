package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.bukkit.sender.BukkitSender;
import cc.dreamcode.command.context.CommandContext;
import cc.dreamcode.command.context.CommandInvokeContext;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class BukkitCommandExecutorWrapper extends Command implements PluginIdentifiableCommand {

    private @Getter final Plugin plugin;
    private final CommandContext context;
    private final DreamCommandExecutor executor;

    public BukkitCommandExecutorWrapper(@NonNull Plugin plugin, @NonNull CommandContext context, @NonNull DreamCommandExecutor executor) {
        super(context.getLabel());

        this.plugin = plugin;
        this.context = context;
        this.executor = executor;

        this.setDescription(context.getDescription());
        this.setAliases(Arrays.asList(context.getAliases()));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        final BukkitSender dreamSender = new BukkitSender(sender);
        final CommandInvokeContext commandInvokeContext = CommandInvokeContext.of(this.context.getLabel(), args);

        return this.executor.invokeMethod(dreamSender, commandInvokeContext);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        final BukkitSender dreamSender = new BukkitSender(sender);
        final CommandInvokeContext commandInvokeContext = CommandInvokeContext.of(this.context.getLabel(), args);

        return this.executor.getSuggestion(dreamSender, commandInvokeContext);
    }
}
