package cc.dreamcode.commandtest.command;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;

@Command(label = "simple", description = "Simple command.")
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
}
