package cc.dreamcode.commandtest.command;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.commandtest.TestCommandSender;

@Command(label = "simple", description = "Simple command.")    // EXAMPLE
public class SimpleCommand extends DreamCommandExecutor {
    @Path(name = "dirt")
    void simpleBlock() {
        System.out.println("[DEBUG] ");
        System.out.println("[DEBUG] Dirt!");
    }

    @Path(name = "block")
    void simpleBlockWithName(@Arg(name = "name") String name, @Arg(name = "name2") String name2) {
        System.out.println("[DEBUG] ");
        System.out.println("[DEBUG] Simple block command execute: " + name + " and " + name2);
    }

    @Path(name = "player")
    void simplePlayer() {
        System.out.println("[DEBUG] ");
        System.out.println("[DEBUG] Simple player command execute");
    }

    @Path(name = "player info")
    void simplePlayerInfo(@Arg(name = "name") String playerInfo) {
        System.out.println("[DEBUG] ");
        System.out.println("[DEBUG] Simple player info: " + playerInfo);
    }

    @Path(name = "reply")
    void simpleReply(@Arg(name = "info") String info, TestCommandSender sender, @Arg(name = "number") int number) {
        sender.sendMessage("[" + sender.getName() + "] Reply word: " + info);
        sender.sendMessage("[" + sender.getName() + "] HasPermission: " + sender.hasPermission("test"));
        sender.sendMessage("[" + sender.getName() + "] Number: " + number);
    }
}
