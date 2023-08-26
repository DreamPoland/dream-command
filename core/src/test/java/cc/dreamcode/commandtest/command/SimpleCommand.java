package cc.dreamcode.commandtest.command;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;

@Command(label = "simple", description = "Simple command.")
public class SimpleCommand implements DreamCommandExecutor {
    @Path
    public void usage() {
        System.out.println("Poprawne uzycie: /simple [block, player]");
    }

    @Path(name = "block")
    public void simpleBlock() {
        System.out.println("Simple block command execute");
    }

    @Path(name = "player")
    public void simplePlayer() {
        System.out.println("Simple player command execute");
    }

    @Path(name = "player")
    public void simplePlayerWithName(@Arg String name) {
        System.out.println("Simple player command execute, named: " + name);
    }
}
