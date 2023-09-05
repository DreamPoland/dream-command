package cc.dreamcode.command.bungee;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.DreamCommandRegistry;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.context.CommandContext;
import cc.dreamcode.command.exception.CommandException;
import lombok.NonNull;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class BungeeCommandRegistry implements DreamCommandRegistry {

    private final Plugin plugin;
    private final BungeeCommand bungeeCommand;

    private final Map<CommandContext, BungeeCommandExecutor> commandMap;

    public BungeeCommandRegistry(@NonNull Plugin plugin, @NonNull BungeeCommand bungeeCommand) {
        this.plugin = plugin;
        this.bungeeCommand = bungeeCommand;

        this.commandMap = new HashMap<>();
    }

    @Override
    public void registerCommand(@NonNull DreamCommandExecutor executor) {
        final Command command = executor.getClass().getAnnotation(Command.class);
        if (command == null) {
            throw new CommandException("Cannot resolve command annotation with context. If you don't want to use annotations, you can also log commands via command registry class.");
        }

        final CommandContext context = CommandContext.of(command);
        this.registerCommand(context, executor);
    }

    @Override
    public void registerCommand(@NonNull CommandContext context, @NonNull DreamCommandExecutor executor) {
        executor.setContext(context);

        executor.setExtensionManager(this.bungeeCommand.getExtensions());
        executor.setHandlerManager(this.bungeeCommand.getHandlers());
        executor.setBindManager(this.bungeeCommand.getBinds());

        final BungeeCommandExecutor wrapper = new BungeeCommandExecutor(context, executor);

        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, wrapper);
        this.commandMap.put(context, wrapper);
    }

    @Override
    public void disposeCommand(@NonNull CommandContext context) {
        BungeeCommandExecutor wrapper = this.commandMap.remove(context);
        this.plugin.getProxy().getPluginManager().unregisterCommand(wrapper);
    }

    @Override
    public void disposeAll() {
        this.commandMap.forEach((context, bungeeCommandExecutor) -> this.disposeCommand(context));
        this.commandMap.clear();
    }
}
