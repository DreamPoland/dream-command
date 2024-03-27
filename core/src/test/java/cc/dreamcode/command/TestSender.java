package cc.dreamcode.command;

import lombok.NonNull;

import java.io.PrintStream;

public class TestSender implements CommandSender<PrintStream> {
    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public boolean hasPermission(@NonNull String permission) {
        return true;
    }

    @Override
    public PrintStream getHandler() {
        return System.out;
    }
}
