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

    @Path(name = "player")
    void simplePlayer(TestCommandSender sender) {
        sender.sendMessage("[" + sender.getName() + "] Simple player command execute");
    }

    @Path(name = "player info")
    void simplePlayerInfo(TestCommandSender sender, @Arg(name = "name") String playerInfo) {
        sender.sendMessage("[" + sender.getName() + "] Simple player info: " + playerInfo);
    }

    @Path(name = "number info")
    void simpleNumberInfo(TestCommandSender sender, @Arg(name = "info") String info, @Arg(name = "number") int number) {
        sender.sendMessage("[" + sender.getName() + "] Info word: " + info);
        sender.sendMessage("[" + sender.getName() + "] Number: " + number);
    }
}
