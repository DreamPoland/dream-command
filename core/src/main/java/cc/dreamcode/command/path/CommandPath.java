package cc.dreamcode.command.path;

import cc.dreamcode.command.DreamCommandContext;
import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.shared.AnnotationUtil;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CommandPath {

    private final String label;
    private final String[] arguments;

    public CommandPath(@NonNull String path) {
        final String[] split = path.split(" ");
        if (split.length == 0) {
            throw new RuntimeException("Cannot find require label in CommandPath input: " + path);
        }

        this.label = split[0];

        final String[] arguments = new String[split.length - 1];
        System.arraycopy(split, 1, arguments, 0, split.length - 1);

        this.arguments = arguments;
    }

    public boolean isValid(@NonNull Command command) {
        return command.label().equals(this.label) || Arrays.asList(command.aliases()).contains(this.label);
    }

    public boolean isValid(@NonNull DreamCommandContext context) {
        return context.getLabel().equals(this.label) || context.getAliases().contains(this.label);
    }

    public boolean isValid(@NonNull Method method) {
        final Path path = method.getAnnotation(Path.class);
        if (path == null) {
            return false;
        }

        final ListBuilder<String[]> availablePathBuilder = new ListBuilder<>();
        availablePathBuilder.add(path.name().split(" "));

        Arrays.stream(path.aliases()).forEach(alias ->
                availablePathBuilder.add(alias.split(" ")));

        final long additionalMethods = AnnotationUtil.countAnnotation(method, Arg.class);
        final List<String[]> availablePaths = availablePathBuilder.build();
        return availablePaths.stream()
                .anyMatch(availablePath -> {
                    if (availablePath.length == this.arguments.length - additionalMethods) {
                        for (int index = 0; index < availablePath.length; index++) {
                            if (!this.arguments[index].equals(availablePath[index])) {
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                });
    }

}
