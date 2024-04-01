package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandContext;
import cc.dreamcode.command.CommandInput;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class BukkitCommandWrapper extends Command implements PluginIdentifiableCommand {

    private final Plugin plugin;
    private final BukkitCommandProvider bukkitCommandProvider;

    public BukkitCommandWrapper(@NonNull Plugin plugin, @NonNull CommandContext context, @NonNull BukkitCommandProvider bukkitCommandProvider) {
        super(context.getName());

        this.plugin = plugin;
        this.bukkitCommandProvider = bukkitCommandProvider;

        this.setDescription(context.getDescription());
        this.setAliases(Arrays.asList(context.getAliases()));
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        final BukkitSender bukkitSender = new BukkitSender(sender);
        final CommandInput commandInput = new CommandInput(this.getLabel(), args, false);

        this.bukkitCommandProvider.call(bukkitSender, commandInput);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        final BukkitSender bukkitSender = new BukkitSender(sender);
        final CommandInput commandInput = new CommandInput(this.getLabel(), args, false);

        return this.bukkitCommandProvider.getSuggestion(bukkitSender, commandInput);
    }
}
