package cc.dreamcode.commandtest.sender;

import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.command.sender.SenderType;
import lombok.NonNull;

import java.io.PrintStream;

public class TestSender implements DreamSender<PrintStream> {
    @Override
    public SenderType getSenderType() {
        return SenderType.CONSOLE;
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

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
