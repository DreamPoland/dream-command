package cc.dreamcode.command.handler;

import cc.dreamcode.command.DreamSender;
import lombok.NonNull;

import java.util.List;

public interface InvalidSenderHandler {

    void handle(@NonNull DreamSender<?> dreamSender, @NonNull List<DreamSender.Type> requireType);
}
