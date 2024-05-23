package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.Async;
import cc.dreamcode.command.annotation.Completion;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.OptArg;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.command.annotation.Sender;
import cc.dreamcode.command.suggestion.SuggestionService;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.utilities.collection.element.Duo;
import cc.dreamcode.utilities.option.Option;
import lombok.Data;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class CommandPathMeta {

    private final CommandMeta commandMeta;

    private final Method method;

    // ignore binds
    private final Map<Integer, Annotation[]> argAnnotations;
    private final Map<Integer, Class<?>> argClasses;
    private final Map<Integer, CommandArgument> argNames;
    private final Map<Integer, CommandArgument> argDisplayNames;

    // all parameters
    private final Map<Integer, Annotation[]> paramAnnotations;
    private final Map<Integer, Class<?>> paramArgs;
    private final Map<Integer, Class<?>> paramMultiArgs;
    private final Map<Integer, Duo<Class<?>, Boolean>> paramOptionalArgs; // true if is optional type
    private final Map<Integer, Class<?>> paramBinds;

    private final String path;
    private final String description;

    private final boolean async;
    private final String[] pathPermissions;
    private final DreamSender.Type[] pathSenderTypes;

    private final CommandExecutor commandExecutor;

    public CommandPathMeta(@NonNull CommandMeta commandMeta, @NonNull Method method, @NonNull Executor executor) {
        this.commandMeta = commandMeta;
        this.method = method;

        this.paramAnnotations = new HashMap<>();
        for (int index = 0; index < this.method.getParameterAnnotations().length; index++) {
            this.paramAnnotations.put(index, this.method.getParameterAnnotations()[index]);
        }

        this.argAnnotations = new HashMap<>();
        this.argClasses = new HashMap<>();
        this.argNames = new HashMap<>();
        this.argDisplayNames = new HashMap<>();

        final AtomicInteger atomicNameIndex = new AtomicInteger();
        for (int index = 0; index < this.method.getParameters().length; index++) {

            this.argAnnotations.put(atomicNameIndex.get(), this.paramAnnotations.get(index));

            final Parameter parameter = this.method.getParameters()[index];
            final Optional<Annotation> optionalArg = Arrays.stream(this.paramAnnotations.get(index))
                    .filter(annotation -> Arg.class.isAssignableFrom(annotation.annotationType()))
                    .findAny();

            if (optionalArg.isPresent()) {
                final Arg arg = (Arg) optionalArg.get();
                final String name = Objects.equals(arg.value(), "") ? parameter.getName() : arg.value();

                this.argClasses.put(atomicNameIndex.get(), parameter.getType());
                this.argNames.put(atomicNameIndex.get(), new CommandArgument(CommandArgument.Type.ARG, name));
                this.argDisplayNames.put(atomicNameIndex.getAndIncrement(), new CommandArgument(CommandArgument.Type.ARG, "<" + name + ">"));

                continue;
            }

            final Optional<Annotation> optionalArgs = Arrays.stream(this.paramAnnotations.get(index))
                    .filter(annotation -> Args.class.isAssignableFrom(annotation.annotationType()))
                    .findAny();

            if (optionalArgs.isPresent()) {
                final Args args = (Args) optionalArgs.get();
                final String name = Objects.equals(args.value(), "") ? parameter.getName() : args.value();

                this.argClasses.put(atomicNameIndex.get(), parameter.getType());
                this.argNames.put(atomicNameIndex.get(), new CommandArgument(CommandArgument.Type.ARGS, name));
                this.argDisplayNames.put(atomicNameIndex.getAndIncrement(), new CommandArgument(CommandArgument.Type.ARGS, "(" + name + ")"));

                continue;
            }

            final Optional<Annotation> optionalOptArg = Arrays.stream(this.paramAnnotations.get(index))
                    .filter(annotation -> OptArg.class.isAssignableFrom(annotation.annotationType()))
                    .findAny();

            if (optionalOptArg.isPresent()) {
                final OptArg optArg = (OptArg) optionalOptArg.get();
                final String name = Objects.equals(optArg.value(), "") ? parameter.getName() : optArg.value();

                Class<?> rawType = parameter.getType();
                if (Optional.class.isAssignableFrom(rawType) || Option.class.isAssignableFrom(rawType)) {

                    ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
                    if (parameterizedType.getActualTypeArguments().length == 1) {
                        Type paramType = parameterizedType.getActualTypeArguments()[0];
                        if (paramType instanceof Class<?>) {
                            rawType = (Class<?>) paramType;
                        }
                    }
                }

                this.argClasses.put(atomicNameIndex.get(), rawType);
                this.argNames.put(atomicNameIndex.get(), new CommandArgument(CommandArgument.Type.OPTIONAL_ARG, name));
                this.argDisplayNames.put(atomicNameIndex.getAndIncrement(), new CommandArgument(CommandArgument.Type.OPTIONAL_ARG, "[" + name + "]"));
            }
        }

        this.paramArgs = new HashMap<>();
        this.paramMultiArgs = new HashMap<>();
        this.paramOptionalArgs = new HashMap<>();
        this.paramBinds = new HashMap<>();

        for (int index = 0; index < this.method.getParameters().length; index++) {

            final Parameter parameter = this.method.getParameters()[index];

            if (Arrays.stream(this.paramAnnotations.get(index))
                    .anyMatch(annotation -> Arg.class.isAssignableFrom(annotation.annotationType()))) {

                int finalIndex = index;
                if (this.paramOptionalArgs.keySet()
                        .stream()
                        .anyMatch(argIndex -> argIndex < finalIndex)) {
                    throw new RuntimeException("@OptionalArg must be specified after @Arg params");
                }

                // arg (transformer)
                this.paramArgs.put(index, parameter.getType());
                continue;
            }

            if (Arrays.stream(this.paramAnnotations.get(index))
                    .anyMatch(annotation -> Args.class.isAssignableFrom(annotation.annotationType()))) {

                // multi-arg (transformer)
                this.paramMultiArgs.put(index, parameter.getType());
                continue;
            }

            if (Arrays.stream(this.paramAnnotations.get(index))
                    .anyMatch(annotation -> OptArg.class.isAssignableFrom(annotation.annotationType()))) {

                Class<?> rawOptionalType = parameter.getType();
                if (!Optional.class.isAssignableFrom(rawOptionalType) && !Option.class.isAssignableFrom(rawOptionalType)) {
                    this.paramOptionalArgs.put(index, new Duo<>(rawOptionalType, false));
                    continue;
                }

                ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
                if (parameterizedType.getActualTypeArguments().length == 1) {
                    Type paramType = parameterizedType.getActualTypeArguments()[0];
                    if (paramType instanceof Class<?>) {
                        this.paramOptionalArgs.put(index, new Duo<>((Class<?>) paramType, true));
                        continue;
                    }
                }

                throw new RuntimeException("Cannot resolve optional value by index " + index);
            }

            // bind
            this.paramBinds.put(index, parameter.getType());
        }

        this.path = executor.path();
        this.description = executor.description();

        this.async = this.method.getAnnotation(Async.class) != null;

        final Permission[] permissionsArray = this.method.getAnnotationsByType(Permission.class);
        this.pathPermissions = Arrays.stream(permissionsArray)
                .map(Permission::value)
                .toArray(String[]::new);

        final Sender[] sendersArray = this.method.getAnnotationsByType(Sender.class);
        this.pathSenderTypes = Arrays.stream(sendersArray)
                .map(Sender::value)
                .toArray(DreamSender.Type[]::new);

        this.commandExecutor = new CommandExecutor(commandMeta, this);
    }

    public boolean isAsync() {
        if (this.commandMeta.isAsync()) {
            return true;
        }

        return this.async;
    }

    public List<String> getPermissions() {
        final List<String> permissions = new ArrayList<>();

        Collections.addAll(permissions, this.commandMeta.getBasePermissions());
        Collections.addAll(permissions, this.pathPermissions);

        return permissions;
    }

    public List<DreamSender.Type> getSendersType() {
        final List<DreamSender.Type> senderTypes = new ArrayList<>();

        Collections.addAll(senderTypes, this.commandMeta.getBaseSenderTypes());
        Collections.addAll(senderTypes, this.pathSenderTypes);

        return senderTypes;
    }

    public String getUsage() {

        final List<String> listBuilder = new ArrayList<>();
        listBuilder.add("/" + this.commandMeta.getCommandContext().getName());

        if (!this.path.isEmpty()) {
            listBuilder.addAll(Arrays.asList(this.path.split(" ")));
        }

        this.argDisplayNames.values()
                .stream()
                .map(CommandArgument::getValue)
                .forEach(listBuilder::add);

        return StringUtil.join(listBuilder, " ");
    }

    public List<String> getSuggestion(@NonNull SuggestionService suggestionService, @NonNull CommandInput commandInput) {

        final ListBuilder<String> listBuilder = new ListBuilder<>();

        final String[] splitPath = this.path.split(" ");
        int splitPathLength = this.path.isEmpty() ? 0 : splitPath.length;

        final String[] arguments = commandInput.getArguments();
        final String joinArguments = StringUtil.join(arguments, " ");
        if (arguments.length > splitPathLength && !joinArguments.startsWith(this.path)) {
            return listBuilder.build();
        }

        if (arguments.length <= splitPathLength && !this.path.startsWith(joinArguments)) {
            return listBuilder.build();
        }

        final int argumentLength = arguments.length - 1 == -1 ? (commandInput.isSpaceAtTheEnd() ? 0 : -1) : commandInput.isSpaceAtTheEnd() ? arguments.length : arguments.length - 1;
        if (argumentLength == -1) {
            return listBuilder.build();
        }

        final int argumentParamLength = argumentLength - splitPathLength;
        if (argumentParamLength < 0) {
            listBuilder.add(splitPath[argumentLength]);
            return listBuilder.build();
        }

        this.paramMultiArgs.forEach((index, classType) -> {
            final Optional<Annotation> optionalAnnotation = Arrays.stream(this.paramAnnotations.get(index))
                    .filter(annotation -> annotation.annotationType().equals(Args.class))
                    .findAny();

            if (!optionalAnnotation.isPresent()) {
                throw new RuntimeException("Annotation @Args not found (critical bug)");
            }

            final Args args = (Args) optionalAnnotation.get();

            final int min = args.min() == -1 ? 0 : args.min();
            final int max = args.max() == -1 ? argumentParamLength : args.max();

            if (argumentParamLength >= min && argumentParamLength <= max) {
                final String displayName = this.argDisplayNames.get(index - this.paramBinds.size()).getValue();
                listBuilder.add(displayName);
            }
        });

        final String lastWord = commandInput.isSpaceAtTheEnd() ? "" : arguments.length == 0 ? "" : arguments[arguments.length - 1];

        if (!this.argNames.containsKey(argumentParamLength)) {
            return listBuilder.build();
        }

        final CommandArgument commandArgument = this.argNames.get(argumentParamLength);
        final Optional<Completion> optionalCompletion = Arrays.stream(this.method.getAnnotationsByType(Completion.class))
                .filter(completion -> Objects.equals(completion.arg(), commandArgument.getValue()))
                .findAny();

        if (!optionalCompletion.isPresent()) {
            final CommandArgument commandDisplayArgument = this.argDisplayNames.get(argumentParamLength);

            if (!commandDisplayArgument.getType().equals(CommandArgument.Type.ARGS)) {
                listBuilder.add(commandDisplayArgument.getValue());
            }

            return listBuilder.build();
        }

        final Completion completion = optionalCompletion.get();
        final Class<?> suggestionParamType = this.argClasses.get(argumentParamLength);

        suggestionService.getSuggestion(suggestionParamType, completion)
                .stream()
                .filter(text -> text.startsWith("[") || text.startsWith("(") || text.startsWith("<") || text.startsWith(lastWord))
                .forEach(listBuilder::add);

        return listBuilder.build();
    }
}
