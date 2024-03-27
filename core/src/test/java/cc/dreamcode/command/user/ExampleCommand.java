package cc.dreamcode.command.user;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Executor;

@Command(name = "example", description = "Example command.")
public class ExampleCommand implements CommandBase {

    @Executor(pattern = "example")
    public void example() {
        System.out.println("example");
    }
}
