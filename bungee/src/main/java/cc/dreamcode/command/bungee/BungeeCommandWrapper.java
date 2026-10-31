package cc.dreamcode.command.bungee;

import cc.dreamcode.command.CommandEntry;
import cc.dreamcode.command.CommandInput;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.logging.Level;

public class BungeeCommandWrapper extends Command implements TabExecutor {

    private final BungeeCommandProvider bungeeCommandProvider;
    private final CommandEntry commandEntry;

    public BungeeCommandWrapper(@NonNull BungeeCommandProvider bungeeCommandProvider, @NonNull CommandEntry commandEntry) {
        super(commandEntry.getName(), null, commandEntry.getAliases());

        this.bungeeCommandProvider = bungeeCommandProvider;
        this.commandEntry = commandEntry;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final BungeeSender bungeeSender = new BungeeSender(sender);
        final CommandInput commandInput = new CommandInput(this.commandEntry.getName(), args, false);

        try {
            this.bungeeCommandProvider.call(bungeeSender, commandInput);
        }
        catch (Exception e) {
            sender.sendMessage(TextComponent.fromLegacyText("§cWystąpił nieoczekiwany błąd podczas wykonywania tej komendy."));
            this.bungeeCommandProvider.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while executing command: " + this.commandEntry.getName(), e);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        final BungeeSender bungeeSender = new BungeeSender(sender);
        final boolean spaceAtTheEnd = args.length > 0 && args[args.length - 1].isEmpty();
        final CommandInput commandInput = new CommandInput(this.commandEntry.getName(), args, spaceAtTheEnd);

        return this.bungeeCommandProvider.getSuggestion(bungeeSender, commandInput);
    }
}
