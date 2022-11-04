package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.ArgumentHandler;
import cc.dreamcode.command.CommandException;
import cc.dreamcode.command.CommandPlatform;
import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import eu.okaeri.injector.Injector;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BukkitCommandHandler extends Command implements PluginIdentifiableCommand, CommandPlatform<CommandSender, Player> {

    @Setter private Plugin plugin;
    @Setter private Injector injector;
    @Getter @Setter private String noPermissionMessage;
    @Getter @Setter private String notPlayerMessage;
    private final List<Class<? extends ArgumentHandler<CommandSender, Player>>> argumentHandlers = new ArrayList<>();

    public BukkitCommandHandler(@NonNull String name, List<String> aliases) {
        super(name);
        if (aliases != null) {
            setAliases(aliases);
        }
    }

    @NonNull
    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull String commandLabel, @NonNull String[] arguments) {
        final CommandPlatform<CommandSender, Player> commandPlatform = this.getCommandMethods(arguments);
        try {
            RequiredPermission requiredPermission = commandPlatform.getClass().getAnnotation(RequiredPermission.class);
            if (requiredPermission != null && !sender.hasPermission(requiredPermission.permission().equals("")
                    ? "rpl." + this.getName()
                    : requiredPermission.permission())) {
                if (this.noPermissionMessage == null) {
                    throw new CommandException("Permission message in command " + this.getName() + " is not provided.");
                }

                throw new CommandException(this.noPermissionMessage);
            }

            RequiredPlayer requiredPlayer = commandPlatform.getClass().getAnnotation(RequiredPlayer.class);
            if (requiredPlayer != null && !(sender instanceof Player)) {
                if (this.notPlayerMessage == null) {
                    throw new CommandException("Not player message in command " + this.getName() + " is not provided.");
                }

                throw new CommandException(this.notPlayerMessage);
            }

            commandPlatform.handle(sender, arguments);
        }
        catch (CommandException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        }
        return true;
    }

    public @NonNull List<String> tabComplete(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> commandCompletions = this.getCommandMethods(args).tab((Player) sender, args);
        if (commandCompletions != null) {
            completions.addAll(commandCompletions);
        }

        this.argumentHandlers
                .stream()
                .map(this.injector::createInstance)
                .filter(argumentHandler -> args.length == argumentHandler.getArg())
                .forEach(argumentHandler -> {
                    RequiredPermission requiredPermission = argumentHandler.getClass().getAnnotation(RequiredPermission.class);
                    if (requiredPermission == null || !sender.hasPermission(requiredPermission.permission())) {
                        return;
                    }

                    completions.add(argumentHandler.getName());
                });

        return completions;
    }

    private CommandPlatform<CommandSender, Player> getCommandMethods(@NonNull String[] args) {
        final AtomicReference<CommandPlatform<CommandSender, Player>> commandMethodsReference = new AtomicReference<>(this);
        this.argumentHandlers
                .stream()
                .map(this.injector::createInstance)
                .filter(argumentHandler -> args.length >= argumentHandler.getArg() &&
                        args[argumentHandler.getArg() - 1].equalsIgnoreCase(argumentHandler.getName()))
                .findFirst()
                .ifPresent(commandMethodsReference::set);

        return commandMethodsReference.get();
    }

    public void addArgument(@NonNull Class<? extends ArgumentHandler<CommandSender, Player>> argumentClass) {
        this.argumentHandlers.add(argumentClass);
    }

}
