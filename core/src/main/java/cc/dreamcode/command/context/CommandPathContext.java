package cc.dreamcode.command.context;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.exception.CommandException;
import cc.dreamcode.command.shared.AnnotationUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Data
@RequiredArgsConstructor(staticName = "of")
public class CommandPathContext {

    private final CommandContext context;
    private final String pathName;
    private final String[] pathAliases;
    private final List<Arg> methodArgs;
    private final List<Args> methodArgsRow;

    public CommandPathContext(@NonNull CommandContext context, @NonNull Path path, @NonNull List<Arg> methodArgs, @NonNull List<Args> methodArgsRow) {
        this.context = context;
        this.pathName = path.name();
        this.pathAliases = path.aliases();
        this.methodArgs = methodArgs;
        this.methodArgsRow = methodArgsRow;
    }

    public CommandPathContext(@NonNull CommandContext context, @NonNull Method method) {
        this.context = context;

        final Path path = method.getAnnotation(Path.class);
        if (path == null) {
            throw new CommandException("Cannot find path annotation on method " + method.getName());
        }

        this.pathName = path.name();
        this.pathAliases = path.aliases();

        this.methodArgs = AnnotationUtil.getAnnotation(method, Arg.class);
        this.methodArgsRow = AnnotationUtil.getAnnotation(method, Args.class);
    }

    public List<String> getPathNameAndAliases() {
        final List<String> pathNames = new ArrayList<>();

        pathNames.add(this.pathName);
        pathNames.addAll(Arrays.asList(this.pathAliases));

        return pathNames;
    }

    public List<String> getMethodArgsNames() {
        return this.methodArgs.stream()
                .map(Arg::name)
                .collect(Collectors.toList());
    }

    public List<String> getMethodArgsRowNames() {
        return this.methodArgsRow.stream()
                .map(Args::name)
                .collect(Collectors.toList());
    }
}
