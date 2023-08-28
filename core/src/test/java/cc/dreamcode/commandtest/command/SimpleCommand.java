package cc.dreamcode.commandtest.command;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.annotation.Usage;

@Command(label = "simple", description = "Simple command.")
public class SimpleCommand implements DreamCommandExecutor {
    @Usage
    public void usage() {
        System.out.println("[DEBUG] ");
        System.out.println("[DEBUG] Poprawne uzycie: /simple [block, player]");
    }

    @Path(name = "block")
    public void simpleBlock() {
        System.out.println("[DEBUG] ");
        System.out.println("[DEBUG] Simple block command execute");
    }

    @Path(name = "player")
    public void simplePlayer() {
        System.out.println("[DEBUG] ");
        System.out.println("[DEBUG] Simple player command execute");
    }

    @Path(name = "player", aliases = "info")
    public void simplePlayerWithName(@Arg String name) {
        System.out.println("[DEBUG] ");
        System.out.println("[DEBUG] Simple player command execute, named: " + name);
    }

    @Path(name = "player", aliases = "info")
    public void simplePlayerWithName(@Arg String name, @Arg String name2) {
        System.out.println("[DEBUG] ");
        System.out.println("[DEBUG] Simple player command execute, named: " + name + " and " + name2);
    }
}
