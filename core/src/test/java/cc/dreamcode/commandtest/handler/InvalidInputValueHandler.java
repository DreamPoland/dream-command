package cc.dreamcode.commandtest.handler;

import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.command.handler.type.InvalidInputValueType;
import lombok.NonNull;

public class InvalidInputValueHandler implements InvalidInputValueType {
    @Override    // EXAMPLE
    public void handle(@NonNull DreamSender<?> sender, @NonNull Class<?> requiredClass, @NonNull String argument, int index) {
        if (requiredClass.isAssignableFrom(Integer.class) || requiredClass.isAssignableFrom(int.class)) {
            sender.sendMessage("Podana liczba nie jest cyfra, argument: " + index + ".");
            return;
        }

        if (requiredClass.isAssignableFrom(Boolean.class) || requiredClass.isAssignableFrom(boolean.class)) {
            sender.sendMessage("Podana wartosc logiczna nie odpowiada na pytanie true/false, argument: " + index + ".");
            return;
        }

        sender.sendMessage("Podano nieprawidlowy argument, potrzebna: " + requiredClass.getSimpleName() + ", argument: " + index + ".");
    }
}
