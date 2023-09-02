package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.context.CommandContext;
import cc.dreamcode.command.context.CommandInvokeContext;
import cc.dreamcode.command.context.CommandPathContext;
import cc.dreamcode.utilities.StringUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public boolean canBeSuggestion(@NonNull CommandContext context, @NonNull Method method) {
        final Path path = method.getAnnotation(Path.class);
        if (path == null) {
            return false;
        }

        return this.canBeSuggestion(new CommandPathContext(context, method));
    }

    public boolean canBeSuggestion(@NonNull CommandPathContext commandPathContext) {
        for (String pathName : commandPathContext.getPathNameAndAliases()) {
            final String[] splitPath = pathName.split(" ");

            // path name suggestion
            if (splitPath.length >= this.getCommandInvokeContext().getArguments().length) {
                final String trimPathName = StringUtil.join(splitPath, " ", 0, this.getCommandInvokeContext().getArguments().length);
                final String joinArguments = StringUtil.join(this.commandInvokeContext.getArguments(), " ");

                if (trimPathName.toLowerCase().startsWith(joinArguments.toLowerCase())) {
                    return true;
                }

                continue;
            }

            // method param suggestion
            final String[] trimPathNameRow = new String[splitPath.length];
            System.arraycopy(this.getCommandInvokeContext().getArguments(), 0, trimPathNameRow, 0, trimPathNameRow.length);

            if (Arrays.equals(splitPath, trimPathNameRow)) {
                return true;
            }
        }

        return false;
    }

    public boolean isValid(@NonNull CommandContext context, @NonNull Method method) {
        final Path path = method.getAnnotation(Path.class);
        if (path == null) {
            return false;
        }

        return this.isValid(new CommandPathContext(context, method));
    }

    public boolean isValid(@NonNull CommandPathContext commandPathContext) {
        return this.isValid(commandPathContext, commandPathContext.getMethodArgs().length);
    }

    public boolean isValid(@NonNull CommandPathContext commandPathContext, int args) {
        for (String pathName : commandPathContext.getPathNameAndAliases()) {
            if (this.isValid(pathName, args)) {
                return true;
            }
        }

        return false;
    }

    public boolean isValid(@NonNull String path, int arguments) {
        if (path.isEmpty()) {
            return arguments == this.commandInvokeContext.getArguments().length;
        }

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
                .filter(arg -> this.isValid(arg, commandPathContext.getMethodArgs().length))
                .findAny();
    }
}
