package cc.dreamcode.command.bungee;

import cc.dreamcode.command.CommandException;
import cc.dreamcode.command.DreamArgument;
import cc.dreamcode.command.DreamCommand;
import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.bungee.BungeeNotice;
import eu.okaeri.injector.Injector;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("unchecked")
public abstract class BungeeCommand extends Command implements TabExecutor, DreamCommand<CommandSender, ProxiedPlayer> {

    @Setter private Injector injector;
    @Getter @Setter private BungeeNotice noPermissionMessage;
    @Getter @Setter private BungeeNotice noPlayerMessage;

    private final List<Class<? extends DreamArgument<CommandSender, ProxiedPlayer>>> argumentHandlers = new ArrayList<>();

    public BungeeCommand(@NonNull String name, String... aliases) {
        super(name, null, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        final DreamCommand<CommandSender, ProxiedPlayer> commandPlatform = this.getCommandMethods(arguments);
        try {
            RequiredPermission requiredPermission = commandPlatform.getClass().getAnnotation(RequiredPermission.class);
            if (requiredPermission != null && !sender.hasPermission(requiredPermission.permission().equals("")
                    ? "rpl." + this.getName()
                    : requiredPermission.permission())) {
                if (this.noPermissionMessage == null) {
                    throw new CommandException(new BungeeNotice(NoticeType.CHAT, "Permission message in command " + this.getName() + " is not provided."));
                }

                throw new CommandException(this.noPermissionMessage);
            }

            RequiredPlayer requiredPlayer = commandPlatform.getClass().getAnnotation(RequiredPlayer.class);
            if (requiredPlayer != null && !(sender instanceof ProxiedPlayer)) {
                if (this.noPlayerMessage == null) {
                    throw new CommandException(new BungeeNotice(NoticeType.CHAT, "Not player message in command " + this.getName() + " is not provided."));
                }

                throw new CommandException(this.noPlayerMessage);
            }

            commandPlatform.content(sender, arguments);
        }
        catch (CommandException e) {
            e.getNotice().send(sender);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> commandCompletions = this.getCommandMethods(args).tab((ProxiedPlayer) sender, args);
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

    private DreamCommand<CommandSender, ProxiedPlayer> getCommandMethods(@NonNull String[] args) {
        final AtomicReference<DreamCommand<CommandSender, ProxiedPlayer>> commandMethodsReference = new AtomicReference<>(this);
        this.argumentHandlers
                .stream()
                .map(this.injector::createInstance)
                .filter(argumentHandler -> args.length >= argumentHandler.getArg() &&
                        args[argumentHandler.getArg() - 1].equalsIgnoreCase(argumentHandler.getName()))
                .findFirst()
                .ifPresent(commandMethodsReference::set);

        return commandMethodsReference.get();
    }

    public void addArgument(@NonNull Class<? extends DreamArgument<CommandSender, ProxiedPlayer>> argumentClass) {
        this.argumentHandlers.add(argumentClass);
    }

    public <T> T createInstance(@NonNull Class<T> type) {
        return this.injector.createInstance(type);
    }

    @Override
    protected void setPermissionMessage(String permissionMessage) {
        this.setNoPermissionMessage(new BungeeNotice(NoticeType.CHAT, permissionMessage));
    }
}
