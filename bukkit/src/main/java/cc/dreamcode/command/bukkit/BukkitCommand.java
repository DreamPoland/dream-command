package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandException;
import cc.dreamcode.command.DreamCommand;
import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.StringColorUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BukkitCommand extends Command implements PluginIdentifiableCommand, DreamCommand<CommandSender> {

    @Setter private Plugin plugin;
    @Getter @Setter private String requiredPermissionMessage;
    @Getter @Setter private String requiredPlayerMessage;

    @Setter private boolean applySubcommandsToTabCompleter = true;
    @Setter private boolean applyTabStartWithFilter = true;

    private final List<BukkitCommand> subcommands = new ArrayList<>();

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
        try {
            RequiredPermission requiredPermission = this.getClass().getAnnotation(RequiredPermission.class);
            if (requiredPermission != null && !sender.hasPermission(requiredPermission.permission().equals("")
                    ? "dream." + this.getName()
                    : requiredPermission.permission())) {
                if (this.requiredPermissionMessage == null) {
                    return false;
                }

                throw new CommandException(StringColorUtil.fixColor(this.requiredPermissionMessage, new MapBuilder<String, Object>()
                        .put("permission", requiredPermission.permission().equals("")
                                ? "dream." + this.getName()
                                : requiredPermission.permission())
                        .build()));
            }

            RequiredPlayer requiredPlayer = this.getClass().getAnnotation(RequiredPlayer.class);
            if (requiredPlayer != null && !(sender instanceof Player)) {
                if (this.requiredPlayerMessage == null) {
                    return false;
                }

                throw new CommandException(StringColorUtil.fixColor(this.requiredPlayerMessage));
            }

            if (arguments.length > 0) {
                final Optional<BukkitCommand> optionalSubcommand = this.subcommands
                        .stream()
                        .filter(bukkitCommand -> bukkitCommand.getName().equalsIgnoreCase(arguments[0]) ||
                                bukkitCommand.getAliases()
                                        .stream()
                                        .anyMatch(alias -> alias.equalsIgnoreCase(arguments[0])))
                        .findAny();

                if (optionalSubcommand.isPresent()) {
                    final BukkitCommand bukkitCommand = optionalSubcommand.get();

                    final String[] subArguments = new String[arguments.length - 1];
                    System.arraycopy(arguments, 1, subArguments, 0, arguments.length - 1);

                    bukkitCommand.execute(sender, commandLabel, subArguments);
                    return true;
                }
            }

            this.content(sender, arguments);
            return true;
        }
        catch (CommandException e) {
            sender.sendMessage(e.getNotice());
            return false;
        }
    }

    public @NonNull List<String> tabComplete(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {
        if (args.length > 0) {
            final Optional<BukkitCommand> optionalSubcommand = this.subcommands
                    .stream()
                    .filter(bukkitCommand -> bukkitCommand.getName().equalsIgnoreCase(args[0]) ||
                            bukkitCommand.getAliases()
                                    .stream()
                                    .anyMatch(alias -> alias.equalsIgnoreCase(args[0])))
                    .findAny();

            if (optionalSubcommand.isPresent()) {
                final BukkitCommand bukkitCommand = optionalSubcommand.get();

                final String[] subArguments = new String[args.length - 1];
                System.arraycopy(args, 1, subArguments, 0, args.length - 1);

                return bukkitCommand.tabComplete(sender, label, subArguments);
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

    public void registerSubcommand(@NonNull BukkitCommand subcommand) {
        subcommand.setPlugin(this.plugin);

        if (this.requiredPermissionMessage != null) {
            subcommand.setRequiredPermissionMessage(this.requiredPermissionMessage);
        }

        if (this.requiredPlayerMessage != null) {
            subcommand.setRequiredPlayerMessage(this.requiredPlayerMessage);
        }

        this.subcommands.add(subcommand);
    }
}
