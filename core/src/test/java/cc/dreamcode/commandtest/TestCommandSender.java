package cc.dreamcode.commandtest;

import cc.dreamcode.command.DreamCommandSender;
import lombok.NonNull;

import java.io.PrintStream;

public class TestCommandSender implements DreamCommandSender<PrintStream> {
    @Override
    public void sendMessage(@NonNull String text) {
        System.out.println(text);
    }

    @Override
    public boolean hasPermission(@NonNull String permission) {
        return true;
    }

    @Override
    public PrintStream getConsumer() {
        return System.out;
    }
}
