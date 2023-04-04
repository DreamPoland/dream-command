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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class BukkitCommand extends Command implements PluginIdentifiableCommand, DreamCommand<CommandSender> {

    @Setter private Plugin plugin;
    @Setter private Injector injector;
    @Getter @Setter private BukkitNotice requiredPermissionMessage;
    @Getter @Setter private BukkitNotice requiredPlayerMessage;

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
                    throw new CommandException(new BukkitNotice(NoticeType.CHAT, "Permission message in command " + this.getName() + " is not provided."));
                }

                throw new CommandException(this.requiredPermissionMessage);
            }

            RequiredPlayer requiredPlayer = this.getClass().getAnnotation(RequiredPlayer.class);
            if (requiredPlayer != null && !(sender instanceof Player)) {
                if (this.requiredPlayerMessage == null) {
                    throw new CommandException(new BukkitNotice(NoticeType.CHAT, "Not player message in command " + this.getName() + " is not provided."));
                }

                throw new CommandException(this.requiredPlayerMessage);
            }

            this.content(sender, arguments);
            return true;
        }
        catch (CommandException e) {
            e.getNotice().send(sender);
            return false;
        }
    }

    public @NonNull List<String> tabComplete(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {
        List<String> tabCompletions = this.tab(sender, args);

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

}
