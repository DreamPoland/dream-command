package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Command;
import lombok.NonNull;

public abstract class DreamCommandImpl implements DreamCommand {

    @Override
    public void registerCommand(@NonNull DreamCommandExecutor executor) {
        final Command command = executor.getClass().getAnnotation(Command.class);
        if (command == null) {
            throw new DreamCommandException("Cannot resolve command annotation with context. If you don't want to use annotations, you can also log commands via command registry class.");
        }

        final DreamCommandContext context = DreamCommandContext.of(command);
        this.getCommandRegistry().registerCommand(context, executor);
    }
}
