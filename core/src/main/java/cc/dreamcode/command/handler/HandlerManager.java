package cc.dreamcode.command.handler;

import lombok.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerManager {

    private final Map<HandlerType, CommandHandler> commandHandlerMap = new HashMap<>();

    public Map<HandlerType, CommandHandler> getHandlerMap() {
        return Collections.unmodifiableMap(this.commandHandlerMap);
    }

    public Optional<CommandHandler> getCommandHandler(@NonNull HandlerType handlerType) {
        return Optional.ofNullable(this.commandHandlerMap.get(handlerType));
    }

    public void registerHandler(@NonNull HandlerType handlerType, @NonNull CommandHandler commandHandler) {
        this.commandHandlerMap.put(handlerType, commandHandler);
    }

    public void unregisterHandler(@NonNull HandlerType handlerType) {
        this.commandHandlerMap.remove(handlerType);
    }
}
