package cc.dreamcode.command.bungee;

import cc.dreamcode.command.CommandException;
import cc.dreamcode.command.DreamCommand;
import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.utilities.builder.ListBuilder;
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
import java.util.stream.Collectors;

public abstract class BungeeCommand extends Command implements TabExecutor, DreamCommand<CommandSender> {

    @Setter private Injector injector;
    @Getter @Setter private String requiredPermissionMessage;
    @Getter @Setter private String requiredPlayerMessage;

    @Setter private boolean applySubcommandsToTabCompleter = false;
    private final List<BungeeCommand> subcommands = new ArrayList<>();
    private final List<Class<? extends BungeeCommand>> subcommandClasses = new ArrayList<>();

    public BungeeCommand(@NonNull String name, String... aliases) {
        super(name, null, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        this.buildSubcommands();

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
        this.buildSubcommands();

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
                                .map(Command::getName)
                                .filter(name -> name.startsWith(args[0]))
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

        return tabCompletions;
    }

    public <T> T createInstance(@NonNull Class<T> type) {
        if (this.injector == null) {
            throw new RuntimeException("Injector cannot be null");
        }

        return this.injector.createInstance(type);
    }

    public void registerSubcommand(@NonNull Class<? extends BungeeCommand> subcommandClass) {
        this.subcommandClasses.add(subcommandClass);
    }

    private void buildSubcommands() {
        if (this.subcommands.size() == this.subcommandClasses.size()) {
            return;
        }

        this.subcommands.clear();
        this.subcommandClasses.forEach(subclass -> {
            final BungeeCommand subcommand = this.createInstance(subclass);
            subcommand.setInjector(this.injector);

            if (this.requiredPermissionMessage != null) {
                subcommand.setRequiredPermissionMessage(this.requiredPermissionMessage);
            }

            if (this.requiredPlayerMessage != null) {
                subcommand.setRequiredPlayerMessage(this.requiredPlayerMessage);
            }

            this.subcommands.add(subcommand);
        });
    }


    @Deprecated
    @Override
    protected void setPermissionMessage(String permissionMessage) {
        this.requiredPermissionMessage = permissionMessage;
    }
}
