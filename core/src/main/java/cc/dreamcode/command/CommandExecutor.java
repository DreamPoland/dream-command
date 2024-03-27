package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.resolver.ObjectResolverService;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.Data;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
public class CommandExecutor {

    private final CommandMeta commandMeta;

    private final Method method;
    private final Map<Integer, Annotation[]> paramAnnotations;
    private final Map<Integer, Class<?>> paramArgs;

    private final String pattern;
    private final String description;

    public CommandExecutor(@NonNull CommandMeta commandMeta, @NonNull Method method, @NonNull Executor executor) {
        this.commandMeta = commandMeta;
        this.method = method;

        this.paramAnnotations = new HashMap<>();
        for (int index = 0; index < this.method.getParameterAnnotations().length; index++) {
            this.paramAnnotations.put(index, this.method.getParameterAnnotations()[index]);
        }
        
        this.paramArgs = new HashMap<>();
        for (int index = 0; index < this.method.getParameterTypes().length; index++) {

            if (Arrays.stream(this.paramAnnotations.get(index))
                    .noneMatch(annotation -> Arg.class.isAssignableFrom(annotation.annotationType()))) {
                continue;
            }

            this.paramArgs.put(index, this.method.getParameterTypes()[index]);
        }

        this.pattern = executor.pattern();
        this.description = executor.description();
    }

    public void invoke(@NonNull ObjectResolverService resolverService, @NonNull CommandInput commandInput) throws InvocationTargetException, IllegalAccessException {

        final ListBuilder<Object> objects = new ListBuilder<>();
        final int patterns = this.pattern.isEmpty() ? 0 : this.pattern.split(" ").length;

        final String[] params = new String[this.paramArgs.size()];
        System.arraycopy(commandInput.getArguments(), patterns, params, 0, params.length);

        for (int index = 0; index < params.length; index++) {

            final String input = params[index];
            final Class<?> paramType = this.paramArgs.get(index);

            final Optional<?> optionalObject = resolverService.resolve(paramType, input);
            if (!optionalObject.isPresent()) {
                throw new RuntimeException("Cannot resolve param " + input + " as a " + paramType.getSimpleName());
            }

            objects.add(optionalObject.get());
        }

        this.method.invoke(this.commandMeta.getCommandBase(), objects.build().toArray());
    }
}
