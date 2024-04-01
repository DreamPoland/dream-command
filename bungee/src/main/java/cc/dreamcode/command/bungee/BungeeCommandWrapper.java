package cc.dreamcode.command.bungee;

import cc.dreamcode.command.CommandContext;
import cc.dreamcode.command.CommandInput;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class BungeeCommandWrapper extends Command implements TabExecutor {

    private final BungeeCommandProvider bungeeCommandProvider;

    public BungeeCommandWrapper(@NonNull CommandContext context, @NonNull BungeeCommandProvider bungeeCommandProvider) {
        super(context.getName(), null, context.getAliases());
        this.bungeeCommandProvider = bungeeCommandProvider;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final BungeeSender bungeeSender = new BungeeSender(sender);
        final CommandInput commandInput = new CommandInput(this.getName(), args, false);

        this.bungeeCommandProvider.call(bungeeSender, commandInput);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        final BungeeSender bungeeSender = new BungeeSender(sender);
        final CommandInput commandInput = new CommandInput(this.getName(), args, false);

        return this.bungeeCommandProvider.getSuggestion(bungeeSender, commandInput);
    }
}
