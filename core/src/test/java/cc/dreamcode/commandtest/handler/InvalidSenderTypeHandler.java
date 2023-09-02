package cc.dreamcode.commandtest.handler;

import cc.dreamcode.command.handler.type.InvalidSenderType;
import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.command.sender.SenderType;
import lombok.NonNull;

public class InvalidSenderTypeHandler implements InvalidSenderType {
    @Override
    public void handle(@NonNull DreamSender<?> sender, @NonNull SenderType requiredSender) {
        sender.sendMessage("Niepoprawny typ sendera, do wykonania komendy potrzebny jest: " + requiredSender);
    }
}
