package cc.dreamcode.command.bungee;

import cc.dreamcode.command.DreamCommand;
import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class BungeeCommand extends Command implements TabExecutor, DreamCommand<CommandSender> {

    @Getter @Setter private Consumer<CommandSender> noPermissionHandler;
    @Getter @Setter private Consumer<CommandSender> notPlayerHandler;

    @Setter private boolean applySubcommandsToTabCompleter = true;
    @Setter private boolean applyTabStartWithFilter = true;

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
                if (this.noPermissionHandler == null) {
                    return;
                }

                throw new BungeeCommandException(this.noPermissionHandler);
            }

            RequiredPlayer requiredPlayer = this.getClass().getAnnotation(RequiredPlayer.class);
            if (requiredPlayer != null && !(sender instanceof ProxiedPlayer)) {
                if (this.notPlayerHandler == null) {
                    return;
                }

                throw new BungeeCommandException(this.notPlayerHandler);
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
        catch (BungeeCommandException e) {
            e.getHandler().accept(sender);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
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

        final List<String> commandTab = this.tab(sender, args);
        final List<String> tabCompletions = new ListBuilder<String>()
                .addAll(commandTab != null
                        ? commandTab
                        : new ArrayList<>())
                .addAll(this.applySubcommandsToTabCompleter && args.length == 1
                        ? this.subcommands.stream()
                                .filter(bukkitCommand -> {
                                    RequiredPermission requiredPermission = bukkitCommand.getClass().getAnnotation(RequiredPermission.class);
                                    return requiredPermission == null || sender.hasPermission(requiredPermission.permission().equals("")
                                            ? "dream." + this.getName()
                                            : requiredPermission.permission());
                                })
                                .map(Command::getName)
                                .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();

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

        return tabCompletions.stream()
                .filter(text -> {
                    if (!this.applyTabStartWithFilter) {
                        return true;
                    }

                    return text.startsWith(args[args.length - 1]);
                })
                .collect(Collectors.toList());
    }

    public void registerSubcommand(@NonNull BungeeCommand subcommand) {
        if (this.noPermissionHandler != null) {
            subcommand.setNoPermissionHandler(this.noPermissionHandler);
        }

        if (this.notPlayerHandler != null) {
            subcommand.setNotPlayerHandler(this.notPlayerHandler);
        }

        this.subcommands.add(subcommand);
    }
}
