package cc.dreamcode.command.bungee;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.bungee.sender.BungeeSender;
import cc.dreamcode.command.context.CommandContext;
import cc.dreamcode.command.context.CommandInvokeContext;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class BungeeCommandExecutor extends Command implements TabExecutor {

    private final CommandContext context;
    private final DreamCommandExecutor executor;

    public BungeeCommandExecutor(@NonNull CommandContext context, @NonNull DreamCommandExecutor executor) {
        super(context.getLabel(), null, context.getAliases());

        this.context = context;
        this.executor = executor;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final BungeeSender dreamSender = new BungeeSender(sender);
        final CommandInvokeContext commandInvokeContext = CommandInvokeContext.of(this.context.getLabel(), args);

        this.executor.invokeMethod(dreamSender, commandInvokeContext);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        final BungeeSender dreamSender = new BungeeSender(sender);
        final CommandInvokeContext commandInvokeContext = CommandInvokeContext.of(this.context.getLabel(), args);

        return this.executor.getSuggestion(dreamSender, commandInvokeContext);
    }
}
