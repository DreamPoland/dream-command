package cc.dreamcode.commandtest.command;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.commandtest.sender.TestSender;

import java.util.Arrays;

@Permission(name = "simpleargs.command")
@Command(label = "simpleargs", description = "Simple args command.")    // EXAMPLE
public class SimpleArgsCommand extends DreamCommandExecutor {
    @Path
    void simpleHelp(TestSender sender) {
        sender.sendMessage("Brak argumentow!");
    }

    @Path(name = "args")
    void simpleArgs(TestSender sender, @Arg(name = "arg") String arg, @Args(name = "args", max = 9) Object[] args) {
        sender.sendMessage("[DEBUG] ");
        sender.sendMessage("[DEBUG] Arg: " + arg);
        sender.sendMessage("[DEBUG] Args: " + Arrays.toString(args));
    }

    @Path(name = "args-ob")
    void simpleMultiArgs(TestSender sender, @Arg(name = "arg") String arg, @Args(name = "args-nick") String[] argsNick, @Args(name = "args") Object[] args) {
        sender.sendMessage("[DEBUG] ");
        sender.sendMessage("[DEBUG] Arg: " + arg);
        sender.sendMessage("[DEBUG] Arg nicks: " + Arrays.toString(argsNick));
        sender.sendMessage("[DEBUG] Args: " + Arrays.toString(args));
    }
}
