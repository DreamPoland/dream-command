package cc.dreamcode.command.bungee;

import cc.dreamcode.command.CommandException;
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

@SuppressWarnings("unchecked")
public abstract class BungeeCommand extends Command implements TabExecutor, DreamCommand<CommandSender> {

    @Setter private Injector injector;
    @Getter @Setter private BungeeNotice requiredPermissionMessage;
    @Getter @Setter private BungeeNotice requiredPlayerMessage;

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
                    throw new CommandException(new BungeeNotice(NoticeType.CHAT, "Permission message in command " + this.getName() + " is not provided."));
                }

                throw new CommandException(this.requiredPermissionMessage);
            }

            RequiredPlayer requiredPlayer = this.getClass().getAnnotation(RequiredPlayer.class);
            if (requiredPlayer != null && !(sender instanceof ProxiedPlayer)) {
                if (this.requiredPlayerMessage == null) {
                    throw new CommandException(new BungeeNotice(NoticeType.CHAT, "Not player message in command " + this.getName() + " is not provided."));
                }

                throw new CommandException(this.requiredPlayerMessage);
            }

            this.content(sender, arguments);
        }
        catch (CommandException e) {
            e.getNotice().send(sender);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
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

    @Override
    protected void setPermissionMessage(String permissionMessage) {
        this.setRequiredPermissionMessage(new BungeeNotice(NoticeType.CHAT, permissionMessage));
    }
}
