package cc.dreamcode.commandtest.command;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.command.annotation.RequireSender;
import cc.dreamcode.command.sender.SenderType;
import cc.dreamcode.commandtest.enums.TestValue;
import cc.dreamcode.commandtest.sender.TestSender;

@Permission(name = "simple.command")
@Command(label = "simple", description = "Simple command.")    // EXAMPLE
public class SimpleCommand extends DreamCommandExecutor {
    @Path
    void simpleHelp(TestSender sender) {
        sender.sendMessage("Brak argumentow!");
    }

    @Path(name = "dirt")
    void simpleBlock(TestSender sender) {
        sender.sendMessage("[DEBUG] ");
        sender.sendMessage("[DEBUG] Dirt!");
    }

    @Path(name = "player")
    void simplePlayer(TestSender sender) {
        sender.sendMessage("[" + sender.getName() + "] Simple player command execute");
    }

    @Path(name = "player info")
    void simplePlayerInfo(TestSender sender, @Arg(name = "name") String playerInfo) {
        sender.sendMessage("[" + sender.getName() + "] Simple player info: " + playerInfo);
    }

    @RequireSender(type = SenderType.PLAYER)
    @Path(name = "number kil")
    void simpleNumberInfo(TestSender sender, @Arg(name = "info3") String info, @Arg(name = "number3") int number) {
        sender.sendMessage("[" + sender.getName() + "] Info word: " + info);
        sender.sendMessage("[" + sender.getName() + "] Number: " + number);
    }

    @RequireSender(type = SenderType.CONSOLE)
    @Permission(name = "simple.numberinfo")
    @Path(name = "number info3")
    void simpleNumberInfo(TestSender sender, @Arg(name = "info2") String info, @Arg(name = "number2") int number, @Arg(name = "value") TestValue value) {
        sender.sendMessage("[" + sender.getName() + "] Info word: " + info);
        sender.sendMessage("[" + sender.getName() + "] Number: " + number);
        sender.sendMessage("[" + sender.getName() + "] Value: " + value.name());
    }
}
