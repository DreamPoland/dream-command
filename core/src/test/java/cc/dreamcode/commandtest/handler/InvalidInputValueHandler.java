package cc.dreamcode.commandtest.handler;

import cc.dreamcode.command.DreamCommandSender;
import cc.dreamcode.command.handler.type.InvalidInputValue;
import lombok.NonNull;

public class InvalidInputValueHandler implements InvalidInputValue {
    @Override    // EXAMPLE
    public void handle(@NonNull DreamCommandSender<?> sender, @NonNull Class<?> requiredClass, @NonNull String argument) {
        if (requiredClass.isAssignableFrom(Integer.class) || requiredClass.isAssignableFrom(int.class)) {
            sender.sendMessage("Podana liczba nie jest cyfra.");
            return;
        }

        if (requiredClass.isAssignableFrom(Boolean.class) || requiredClass.isAssignableFrom(boolean.class)) {
            sender.sendMessage("Podana wartosc logiczna nie odpowiada na pytanie true/false.");
            return;
        }

        sender.sendMessage("Podano nieprawidlowy argument, potrzebna: " + requiredClass.getSimpleName());
    }
}
