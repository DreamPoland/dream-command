package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.context.CommandContext;
import cc.dreamcode.command.context.CommandInvokeContext;
import cc.dreamcode.command.context.CommandPathContext;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Data
@RequiredArgsConstructor
public class DreamCommandValidator {

    private final CommandInvokeContext commandInvokeContext;

    public boolean isValid(@NonNull Command command) {
        return command.label().equals(this.commandInvokeContext.getLabel()) || Arrays.stream(command.aliases())
                .anyMatch(alias -> alias.equalsIgnoreCase(this.commandInvokeContext.getLabel()));
    }

    public boolean isValid(@NonNull CommandContext context) {
        return context.getLabel().equals(this.commandInvokeContext.getLabel()) || Arrays.stream(context.getAliases())
                .anyMatch(alias -> alias.equalsIgnoreCase(this.commandInvokeContext.getLabel()));
    }

    public boolean isSimilar(@NonNull CommandContext context, @NonNull Method method) {
        final Path path = method.getAnnotation(Path.class);
        if (path == null) {
            return false;
        }

        return this.isSimilar(new CommandPathContext(context, method));
    }

    public boolean isSimilar(@NonNull CommandPathContext commandPathContext) {
        for (String pathName : commandPathContext.getPathNameAndAliases()) {
            if (this.isSimilar(pathName, commandPathContext.getMethodArgs().length)) {
                return true;
            }
        }

        return false;
    }

    public boolean isSimilar(@NonNull String path, int arguments) {
        final String[] splitPath = path.split(" ");
        if (splitPath.length + arguments != this.commandInvokeContext.getArguments().length) {
            return false;
        }

        for (int index = 0; index < splitPath.length; index++) {
            if (!this.commandInvokeContext.getArguments()[index].equals(splitPath[index])) {
                return false;
            }
        }

        return true;
    }

    public Optional<String> getUsingPathName(@NonNull CommandPathContext commandPathContext) {
        final List<String> args = commandPathContext.getPathNameAndAliases();

        return args.stream()
                .filter(arg -> this.isSimilar(arg, commandPathContext.getMethodArgs().length))
                .findAny();
    }

    /**
     * Get a most similar command path, which can used in invalid usage handler.
     */
    public Optional<CommandPathContext> findSimilarPath(@NonNull List<CommandPathContext> commandPathContextList) {
        final AtomicReference<CommandPathContext> mostSimilar = new AtomicReference<>();
        for (int invokeIndex = 0; invokeIndex < this.commandInvokeContext.getArguments().length; invokeIndex++) {
            for (CommandPathContext pathContext : commandPathContextList) {

                for (String pathName : pathContext.getPathNameAndAliases()) {
                    final String[] split = pathName.split(" ");

                    final String invokeParam = this.commandInvokeContext.getArguments()[invokeIndex];
                    if (split.length <= invokeIndex) {
                        continue;
                    }

                    final String pathParam = split[invokeIndex];
                    if (!invokeParam.equalsIgnoreCase(pathParam)) {
                        continue;
                    }

                    mostSimilar.set(pathContext);
                }
            }
        }

        return Optional.ofNullable(mostSimilar.get());
    }
}
