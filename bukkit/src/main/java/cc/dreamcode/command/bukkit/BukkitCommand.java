package cc.dreamcode.command.bukkit;

import cc.dreamcode.command.CommandException;
import cc.dreamcode.command.DreamCommand;
import cc.dreamcode.command.annotations.RequiredPermission;
import cc.dreamcode.command.annotations.RequiredPlayer;
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

public abstract class BukkitCommand extends Command implements PluginIdentifiableCommand, DreamCommand<CommandSender> {

    @Setter private Plugin plugin;
    @Setter private Injector injector;
    @Getter @Setter private String requiredPermissionMessage;
    @Getter @Setter private String requiredPlayerMessage;

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

            this.content(sender, arguments);
            return true;
        }
        catch (CommandException e) {
            sender.sendMessage(e.getNotice());
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
