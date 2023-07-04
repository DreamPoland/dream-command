package cc.dreamcode.command.bungee;

import cc.dreamcode.command.CommandException;
import cc.dreamcode.command.DreamCommand;
import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bungee.ChatUtil;
import eu.okaeri.injector.Injector;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class BungeeCommand extends Command implements TabExecutor, DreamCommand<CommandSender> {

    @Setter private Injector injector;
    @Getter @Setter private String requiredPermissionMessage;
    @Getter @Setter private String requiredPlayerMessage;

    private final List<BungeeCommand> subcommands = new ArrayList<>();

    public BungeeCommand(@NonNull String name, String... aliases) {
        super(name, null, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        try {
            RequiredPermission requiredPermission = this.getClass().getAnnotation(RequiredPermission.class);
            if (requiredPermission != null && !sender.hasPermission(requiredPermission.permission().equals("")
                    ? "dream." + this.getName()
                    : requiredPermission.permission())) {
                if (this.requiredPermissionMessage == null) {
                    return;
                }

                throw new CommandException(ChatUtil.fixColor(this.requiredPermissionMessage, new MapBuilder<String, Object>()
                        .put("permission", requiredPermission.permission().equals("")
                                ? "dream." + this.getName()
                                : requiredPermission.permission())
                        .build()));
            }

            RequiredPlayer requiredPlayer = this.getClass().getAnnotation(RequiredPlayer.class);
            if (requiredPlayer != null && !(sender instanceof ProxiedPlayer)) {
                if (this.requiredPlayerMessage == null) {
                    return;
                }

                throw new CommandException(ChatUtil.fixColor(this.requiredPlayerMessage));
            }

            if (arguments.length > 0) {
                final Optional<BungeeCommand> optionalSubcommand = this.subcommands
                        .stream()
                        .filter(bungeeCommand -> bungeeCommand.getName().equalsIgnoreCase(arguments[0]) ||
                                Arrays.stream(bungeeCommand.getAliases())
                                        .anyMatch(alias -> alias.equalsIgnoreCase(arguments[0])))
                        .findAny();

                if (optionalSubcommand.isPresent()) {
                    final BungeeCommand bungeeCommand = optionalSubcommand.get();

                    final String[] subArguments = new String[arguments.length - 1];
                    System.arraycopy(arguments, 1, subArguments, 0, arguments.length - 1);

                    bungeeCommand.execute(sender, subArguments);
                    return;
                }
            }

            this.content(sender, arguments);
        }
        catch (CommandException e) {
            sender.sendMessage(new TextComponent(e.getNotice()));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> tabCompletions = this.tab(sender, args);

        if (args.length > 0) {
            final Optional<BungeeCommand> optionalSubcommand = this.subcommands
                    .stream()
                    .filter(bungeeCommand -> bungeeCommand.getName().equalsIgnoreCase(args[0]) ||
                            Arrays.stream(bungeeCommand.getAliases())
                                    .anyMatch(alias -> alias.equalsIgnoreCase(args[0])))
                    .findAny();

            if (optionalSubcommand.isPresent()) {
                final BungeeCommand bungeeCommand = optionalSubcommand.get();

                final String[] subArguments = new String[args.length - 1];
                System.arraycopy(args, 1, subArguments, 0, args.length - 1);

                return bungeeCommand.onTabComplete(sender, subArguments);
            }
        }


        if (tabCompletions == null ||
                tabCompletions.isEmpty()) {
            return new ArrayList<>();
        }

        RequiredPermission requiredPermission = this.getClass().getAnnotation(RequiredPermission.class);
        if (requiredPermission != null && !sender.hasPermission(requiredPermission.permission().equals("")
                ? "dream." + this.getName()
                : requiredPermission.permission())) {
            return new ArrayList<>();
        }

        return tabCompletions;
    }

    public <T> T createInstance(@NonNull Class<T> type) {
        return this.injector.createInstance(type);
    }

    public void registerSubcommand(@NonNull Class<? extends BungeeCommand> subcommandClass) {
        final BungeeCommand subcommand = this.createInstance(subcommandClass);
        this.subcommands.add(subcommand);
    }

    @Deprecated
    @Override
    protected void setPermissionMessage(String permissionMessage) {
        this.requiredPermissionMessage = permissionMessage;
    }
}
