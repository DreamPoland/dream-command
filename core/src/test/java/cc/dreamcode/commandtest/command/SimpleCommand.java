package cc.dreamcode.commandtest.command;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.command.annotation.RequireSender;
import cc.dreamcode.command.sender.SenderType;
import cc.dreamcode.commandtest.enums.Answer;
import cc.dreamcode.commandtest.sender.TestSender;

@Permission(name = "simple.command")
@Command(label = "simple", description = "Simple command.")
public class SimpleCommand extends DreamCommandExecutor {
    @Path
    void simpleHelp(TestSender sender) {
        sender.sendMessage("Brak argumentow!");
    }

    @Path(name = "dirt")
    void simpleBlock(TestSender sender) {
        sender.sendMessage("Dirt!");
    }

    @Path(name = "player")
    void simplePlayerInput(TestSender sender, @Arg(name = "name") String playerName) {
        sender.sendMessage("[" + sender.getName() + "] Player name: " + playerName);
    }

    @Path(name = "player all")
    void simplePlayerInput(TestSender sender) {
        sender.sendMessage("[" + sender.getName() + "] Player name: from all");
    }

    @Path(name = "broadcast")
    void simpleBroadcast(TestSender sender, @Args(name = "message") String message) {
        sender.sendMessage("[BROADCAST] " + message);
    }

    @Permission(name = "simple.answer")
    @Path(name = "answer")
    void simpleAnswer(TestSender sender, @Arg(name = "answer") Answer answer) {
        sender.sendMessage("You answered: " + answer);
    }

    @RequireSender(type = SenderType.PLAYER)
    @Path(name = "words")
    void simpleWords(TestSender sender, @Args(name = "words") String[] words) {
        for (int index = 0; index < words.length; index++) {
            final String word = words[index];

            sender.sendMessage("Word number " + index + ": " + word);
        }
    }
}
