package cc.dreamcode.commandtest.command;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.commandtest.TestCommandSender;
import cc.dreamcode.commandtest.TestValue;

@Permission(name = "simple.command")
@Command(label = "simple", description = "Simple command.")    // EXAMPLE
public class SimpleCommand extends DreamCommandExecutor {
    @Path
    void simpleHelp(TestCommandSender sender) {
        sender.sendMessage("Brak argumentow!");
    }

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
    void simpleNumberInfo(TestCommandSender sender, @Arg(name = "info3") String info, @Arg(name = "number3") int number) {
        sender.sendMessage("[" + sender.getName() + "] Info word: " + info);
        sender.sendMessage("[" + sender.getName() + "] Number: " + number);
    }

    @Permission(name = "simple.numberinfo")
    @Path(name = "number info")
    void simpleNumberInfo(TestCommandSender sender, @Arg(name = "info2") String info, @Arg(name = "number2") int number, @Arg(name = "value") TestValue value) {
        sender.sendMessage("[" + sender.getName() + "] Info word: " + info);
        sender.sendMessage("[" + sender.getName() + "] Number: " + number);
        sender.sendMessage("[" + sender.getName() + "] Value: " + value.name());
    }
}
