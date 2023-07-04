package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandException;
import cc.dreamcode.command.DreamCommand;
import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
import cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.ChatUtil;
import eu.okaeri.injector.Injector;
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
    @Setter private Injector injector;
    @Getter @Setter private String requiredPermissionMessage;
    @Getter @Setter private String requiredPlayerMessage;

    @Setter private boolean applySubcommandsToTabCompleter = false;
    private final List<BukkitCommand> subcommands = new ArrayList<>();
    private final List<Class<? extends BukkitCommand>> subcommandClasses = new ArrayList<>();

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
        this.buildSubcommands();

        try {
            RequiredPermission requiredPermission = this.getClass().getAnnotation(RequiredPermission.class);
            if (requiredPermission != null && !sender.hasPermission(requiredPermission.permission().equals("")
                    ? "dream." + this.getName()
                    : requiredPermission.permission())) {
                if (this.requiredPermissionMessage == null) {
                    return false;
                }

                throw new CommandException(ChatUtil.fixColor(this.requiredPermissionMessage, new MapBuilder<String, Object>()
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

                throw new CommandException(ChatUtil.fixColor(this.requiredPlayerMessage));
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
        this.buildSubcommands();

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

        final List<String> tabCompletions = new ListBuilder<String>()
                .addAll(this.tab(sender, args))
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

    public void registerSubcommand(@NonNull Class<? extends BukkitCommand> subcommandClass) {
        this.subcommandClasses.add(subcommandClass);
    }

    private void buildSubcommands() {
        if (this.subcommands.size() == this.subcommandClasses.size()) {
            return;
        }

        this.subcommands.clear();
        this.subcommandClasses.forEach(subclass -> {
            final BukkitCommand subcommand = this.createInstance(subclass);
            subcommand.setPlugin(this.plugin);
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

}
