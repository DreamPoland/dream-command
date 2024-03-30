package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.Completion;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.OptArg;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.command.annotation.Sender;
import cc.dreamcode.command.suggestion.SuggestionService;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.Data;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
    private final Map<Integer, CommandArgument> paramNames;
    private final Map<Integer, CommandArgument> paramDisplayNames;

    private final Map<Integer, Annotation[]> paramAnnotations;
    private final Map<Integer, Class<?>> paramArgs;
    private final Map<Integer, Class<?>> paramMultiArgs;
    private final Map<Integer, Class<?>> paramOptionalArgs;
    private final Map<Integer, Class<?>> paramBinds;

    private final String path;
    private final String description;

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

        this.paramNames = new HashMap<>();
        this.paramDisplayNames = new HashMap<>();
        final AtomicInteger atomicNameIndex = new AtomicInteger();
        for (int index = 0; index < this.method.getParameters().length; index++) {
            final Parameter parameter = this.method.getParameters()[index];
            final Optional<Annotation> optionalArg = Arrays.stream(this.paramAnnotations.get(index))
                    .filter(annotation -> Arg.class.isAssignableFrom(annotation.annotationType()))
                    .findAny();

            if (optionalArg.isPresent()) {
                final Arg arg = (Arg) optionalArg.get();
                final String name = Objects.equals(arg.name(), "") ? parameter.getName() : arg.name();

                this.paramNames.put(atomicNameIndex.get(), new CommandArgument(CommandArgument.Type.ARG, name));
                this.paramDisplayNames.put(atomicNameIndex.getAndIncrement(), new CommandArgument(CommandArgument.Type.ARG, "<" + name + ">"));

                continue;
            }

            final Optional<Annotation> optionalArgs = Arrays.stream(this.paramAnnotations.get(index))
                    .filter(annotation -> Args.class.isAssignableFrom(annotation.annotationType()))
                    .findAny();

            if (optionalArgs.isPresent()) {
                final Args args = (Args) optionalArgs.get();
                final String name = Objects.equals(args.name(), "") ? parameter.getName() : args.name();

                this.paramNames.put(atomicNameIndex.get(), new CommandArgument(CommandArgument.Type.ARGS, name));
                this.paramDisplayNames.put(atomicNameIndex.getAndIncrement(), new CommandArgument(CommandArgument.Type.ARGS, "(" + name + ")"));

                continue;
            }

            final Optional<Annotation> optionalOptArg = Arrays.stream(this.paramAnnotations.get(index))
                    .filter(annotation -> OptArg.class.isAssignableFrom(annotation.annotationType()))
                    .findAny();

            if (optionalOptArg.isPresent()) {
                final OptArg optArg = (OptArg) optionalOptArg.get();
                final String name = Objects.equals(optArg.name(), "") ? parameter.getName() : optArg.name();

                this.paramNames.put(atomicNameIndex.get(), new CommandArgument(CommandArgument.Type.OPTIONAL_ARG, name));
                this.paramDisplayNames.put(atomicNameIndex.getAndIncrement(), new CommandArgument(CommandArgument.Type.OPTIONAL_ARG, "[" + name + "]"));
            }
        }

        this.paramArgs = new HashMap<>();
        this.paramMultiArgs = new HashMap<>();
        this.paramOptionalArgs = new HashMap<>();
        this.paramBinds = new HashMap<>();
        for (int index = 0; index < this.method.getParameterTypes().length; index++) {

            if (Arrays.stream(this.paramAnnotations.get(index))
                    .anyMatch(annotation -> Arg.class.isAssignableFrom(annotation.annotationType()))) {

                int finalIndex = index;
                if (this.paramOptionalArgs.keySet()
                        .stream()
                        .anyMatch(argIndex -> argIndex < finalIndex)) {
                    throw new RuntimeException("@OptionalArg must be specified after @Arg params");
                }

                // arg (transformer)
                this.paramArgs.put(index, this.method.getParameterTypes()[index]);
                continue;
            }

            if (Arrays.stream(this.paramAnnotations.get(index))
                    .anyMatch(annotation -> Args.class.isAssignableFrom(annotation.annotationType()))) {

                // multi-arg (transformer)
                this.paramMultiArgs.put(index, this.method.getParameterTypes()[index]);
                continue;
            }

            if (Arrays.stream(this.paramAnnotations.get(index))
                    .anyMatch(annotation -> OptArg.class.isAssignableFrom(annotation.annotationType()))) {

                this.paramOptionalArgs.put(index, this.method.getParameterTypes()[index]);
                continue;
            }

            // bind
            this.paramBinds.put(index, this.method.getParameterTypes()[index]);
        }

        this.path = executor.path();
        this.description = executor.description();

        final Permission[] permissionsArray = this.method.getAnnotationsByType(Permission.class);
        this.pathPermissions = Arrays.stream(permissionsArray)
                .map(Permission::name)
                .toArray(String[]::new);

        final Sender[] sendersArray = this.method.getAnnotationsByType(Sender.class);
        this.pathSenderTypes = Arrays.stream(sendersArray)
                .map(Sender::type)
                .toArray(DreamSender.Type[]::new);

        this.commandExecutor = new CommandExecutor(commandMeta, this);
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

        this.paramDisplayNames.values()
                .stream()
                .map(CommandArgument::getValue)
                .forEach(listBuilder::add);

        return StringUtil.join(listBuilder, " ");
    }

    public List<String> getSuggestion(@NonNull SuggestionService suggestionService, @NonNull CommandInput commandInput) {

        final String[] arguments = commandInput.getArguments();
        final int argumentLength = arguments.length - 1 == -1 ? (commandInput.isSpaceAtTheEnd() ? 0 : -1) : commandInput.isSpaceAtTheEnd() ? arguments.length : arguments.length - 1;

        final ListBuilder<String> listBuilder = new ListBuilder<>();

        this.paramMultiArgs.forEach((index, classType) -> {
            final Optional<Annotation> optionalAnnotation = Arrays.stream(this.paramAnnotations.get(index))
                    .filter(annotation -> annotation.annotationType().equals(Args.class))
                    .findAny();

            if (!optionalAnnotation.isPresent()) {
                throw new RuntimeException("Annotation @Args not found (critical bug)");
            }

            final Args args = (Args) optionalAnnotation.get();

            final int min = args.min() == -1 ? 0 : args.min();
            final int max = args.max() == -1 ? argumentLength : args.max();

            if (argumentLength >= min && argumentLength <= max) {
                final String displayName = this.paramDisplayNames.get(index).getValue();
                listBuilder.add(displayName);
            }
        });

        final String lastWord = arguments.length == 0 ? "" : arguments[arguments.length - 1];

        if (!this.paramNames.containsKey(argumentLength)) {
            return listBuilder.build();
        }

        final CommandArgument commandArgument = this.paramNames.get(argumentLength);
        final Optional<Completion> optionalCompletion = Arrays.stream(this.method.getAnnotationsByType(Completion.class))
                .filter(completion -> Objects.equals(completion.arg(), commandArgument.getValue()))
                .findAny();

        if (!optionalCompletion.isPresent()) {
            if (this.paramDisplayNames.containsKey(argumentLength)) {
                final CommandArgument commandDisplayArgument = this.paramDisplayNames.get(argumentLength);

                if (!commandDisplayArgument.getType().equals(CommandArgument.Type.ARGS)) {
                    listBuilder.add(commandDisplayArgument.getValue());
                }
            }

            return listBuilder.build();
        }

        final Completion completion = optionalCompletion.get();
        suggestionService.getSuggestion(completion)
                .stream()
                .filter(text -> text.startsWith("[") || text.startsWith("(") || text.startsWith("<") || text.startsWith(lastWord))
                .forEach(listBuilder::add);

        return listBuilder.build();
    }
}
