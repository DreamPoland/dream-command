package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandEntry;
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
    private final CommandEntry commandEntry;
    private final BukkitCommandProvider bukkitCommandProvider;

    public BukkitCommandWrapper(@NonNull Plugin plugin, @NonNull CommandEntry commandEntry, @NonNull BukkitCommandProvider bukkitCommandProvider) {
        super(commandEntry.getName());

        this.plugin = plugin;
        this.commandEntry = commandEntry;
        this.bukkitCommandProvider = bukkitCommandProvider;

        this.setDescription(commandEntry.getDescription());
        this.setAliases(Arrays.asList(commandEntry.getAliases()));
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        final BukkitSender bukkitSender = new BukkitSender(sender);
        final CommandInput commandInput = new CommandInput(this.commandEntry.getName(), args, false);

        this.bukkitCommandProvider.call(bukkitSender, commandInput);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        final BukkitSender bukkitSender = new BukkitSender(sender);
        final CommandInput commandInput = new CommandInput(this.commandEntry.getName(), args, false);

        return this.bukkitCommandProvider.getSuggestion(bukkitSender, commandInput);
    }
}
