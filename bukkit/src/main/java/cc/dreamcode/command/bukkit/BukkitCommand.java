package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandException;
import cc.dreamcode.command.DreamCommand;
import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.bukkit.BukkitNotice;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BukkitCommand extends Command implements PluginIdentifiableCommand, DreamCommand<CommandSender, Player> {

    @Setter private Plugin plugin;
    @Setter private Injector injector;
    @Getter @Setter private BukkitNotice noPermissionMessage;
    @Getter @Setter private BukkitNotice noPlayerMessage;
    private final List<Class<? extends BukkitArgument>> argumentHandlers = new ArrayList<>();

    public BukkitCommand(@NonNull String name, String... aliases) {
        super(name);
        if (aliases != null) {
            setAliases(Arrays.asList(aliases));
        }
    }

    @NonNull
    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull String commandLabel, @NonNull String[] arguments) {
        final DreamCommand<CommandSender, Player> commandPlatform = this.getCommandMethods(arguments);
        try {
            RequiredPermission requiredPermission = commandPlatform.getClass().getAnnotation(RequiredPermission.class);
            if (requiredPermission != null && !sender.hasPermission(requiredPermission.permission().equals("")
                    ? "rpl." + this.getName()
                    : requiredPermission.permission())) {
                if (this.noPermissionMessage == null) {
                    throw new CommandException(new BukkitNotice(NoticeType.CHAT, "Permission message in command " + this.getName() + " is not provided."));
                }

                throw new CommandException(this.noPermissionMessage);
            }

            RequiredPlayer requiredPlayer = commandPlatform.getClass().getAnnotation(RequiredPlayer.class);
            if (requiredPlayer != null && !(sender instanceof Player)) {
                if (this.noPlayerMessage == null) {
                    throw new CommandException(new BukkitNotice(NoticeType.CHAT, "Not player message in command " + this.getName() + " is not provided."));
                }

                throw new CommandException(this.noPlayerMessage);
            }

            commandPlatform.content(sender, arguments);
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

    private DreamCommand<CommandSender, Player> getCommandMethods(@NonNull String[] args) {
        final AtomicReference<DreamCommand<CommandSender, Player>> commandMethodsReference = new AtomicReference<>(this);
        this.argumentHandlers
                .stream()
                .map(this.injector::createInstance)
                .filter(argumentHandler -> args.length >= argumentHandler.getArg() &&
                        args[argumentHandler.getArg() - 1].equalsIgnoreCase(argumentHandler.getName()))
                .findFirst()
                .ifPresent(commandMethodsReference::set);

        return commandMethodsReference.get();
    }

    public void addArgument(@NonNull Class<? extends BukkitArgument> argumentClass) {
        this.argumentHandlers.add(argumentClass);
    }

    public <T> T createInstance(@NonNull Class<T> type) {
        return this.injector.createInstance(type);
    }

}
