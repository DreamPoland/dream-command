package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.bind.BindService;
import cc.dreamcode.command.resolver.ResolverService;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.Data;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class CommandExecutor {

    private final CommandMeta commandMeta;

    private final Method method;
    private final Map<Integer, Annotation[]> paramAnnotations;
    private final Map<Integer, Class<?>> paramArgs;
    private final Map<Integer, Class<?>> paramBinds;

    private final String path;
    private final String description;

    public CommandExecutor(@NonNull CommandMeta commandMeta, @NonNull Method method, @NonNull Executor executor) {
        this.commandMeta = commandMeta;
        this.method = method;

        this.paramAnnotations = new HashMap<>();
        for (int index = 0; index < this.method.getParameterAnnotations().length; index++) {
            this.paramAnnotations.put(index, this.method.getParameterAnnotations()[index]);
        }
        
        this.paramArgs = new HashMap<>();
        this.paramBinds = new HashMap<>();
        for (int index = 0; index < this.method.getParameterTypes().length; index++) {

            if (Arrays.stream(this.paramAnnotations.get(index))
                    .noneMatch(annotation -> Arg.class.isAssignableFrom(annotation.annotationType()))) {

                // bind
                this.paramBinds.put(index, this.method.getParameterTypes()[index]);
                continue;
            }

            // arg (transformer)
            this.paramArgs.put(index, this.method.getParameterTypes()[index]);
        }

        this.path = executor.path();
        this.description = executor.description();
    }

    public void invoke(@NonNull ResolverService resolverService, @NonNull BindService bindService, @NonNull CommandInput commandInput, @NonNull CommandSender<?> sender) throws InvocationTargetException, IllegalAccessException {

        final ListBuilder<Object> objects = new ListBuilder<>();
        final int patterns = this.path.isEmpty() ? 0 : this.path.split(" ").length;

        final String[] params = new String[this.paramArgs.size()];
        System.arraycopy(commandInput.getArguments(), patterns, params, 0, params.length);

        final AtomicInteger atomicArg = new AtomicInteger();
        final List<Class<?>> argClasses = new ArrayList<>(this.paramArgs.values());
        for (int index = 0; index < this.method.getParameterCount(); index++) {

            if (this.paramArgs.containsKey(index)) {
                final String input = params[atomicArg.get()];
                final Class<?> paramType = argClasses.get(atomicArg.get());

                final Optional<?> optionalObject = resolverService.resolve(paramType, input);
                if (!optionalObject.isPresent()) {
                    throw new RuntimeException("Cannot resolve param " + input + " as a " + paramType.getSimpleName());
                }

                objects.add(optionalObject.get());
                atomicArg.incrementAndGet();
            }

            if (this.paramBinds.containsKey(index)) {
                final Class<?> paramType = this.paramBinds.get(index);
                final Optional<?> optionalObject = bindService.resolveBind(paramType, sender);

                if (!optionalObject.isPresent()) {
                    throw new RuntimeException("Cannot resolve bind: " + paramType.getSimpleName());
                }

                objects.add(optionalObject.get());
            }
        }

        this.method.invoke(this.commandMeta.getCommandBase(), objects.build().toArray());
    }
}
