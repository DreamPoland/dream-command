package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.Completion;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.OptArg;
import cc.dreamcode.command.suggestion.SuggestionService;
import cc.dreamcode.utilities.StringUtil;
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
import java.util.stream.Collectors;

@Data
public class CommandPathMeta {

    private final CommandMeta commandMeta;

    private final Method method;
    private final Map<Integer, String> paramNames;
    private final Map<Integer, Annotation[]> paramAnnotations;
    private final Map<Integer, Class<?>> paramArgs;
    private final Map<Integer, Class<?>> paramMultiArgs;
    private final Map<Integer, Class<Optional<?>>> paramOptionalArgs;
    private final Map<Integer, Class<?>> paramBinds;

    private final String path;
    private final String description;

    private final CommandExecutor commandExecutor;

    @SuppressWarnings("unchecked")
    public CommandPathMeta(@NonNull CommandMeta commandMeta, @NonNull Method method, @NonNull Executor executor) {
        this.commandMeta = commandMeta;
        this.method = method;

        this.paramAnnotations = new HashMap<>();
        for (int index = 0; index < this.method.getParameterAnnotations().length; index++) {
            this.paramAnnotations.put(index, this.method.getParameterAnnotations()[index]);
        }

        this.paramNames = new HashMap<>();
        for (int index = 0; index < this.method.getParameters().length; index++) {
            final Parameter parameter = this.method.getParameters()[index];

            final Optional<Annotation> optionalArg = Arrays.stream(this.paramAnnotations.get(index))
                    .filter(annotation -> Arg.class.isAssignableFrom(annotation.annotationType()))
                    .findAny();

            if (optionalArg.isPresent()) {
                final Arg arg = (Arg) optionalArg.get();
                this.paramNames.put(index, arg.name());

                continue;
            }

            final Optional<Annotation> optionalArgs = Arrays.stream(this.paramAnnotations.get(index))
                    .filter(annotation -> Args.class.isAssignableFrom(annotation.annotationType()))
                    .findAny();

            if (optionalArgs.isPresent()) {
                final Args args = (Args) optionalArgs.get();
                this.paramNames.put(index, args.name());

                continue;
            }

            final Optional<Annotation> optionalOptArg = Arrays.stream(this.paramAnnotations.get(index))
                    .filter(annotation -> OptArg.class.isAssignableFrom(annotation.annotationType()))
                    .findAny();

            if (optionalOptArg.isPresent()) {
                final OptArg optArg = (OptArg) optionalOptArg.get();
                this.paramNames.put(index, optArg.name());

                continue;
            }

            this.paramNames.put(index, parameter.getName());
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

                this.paramOptionalArgs.put(index, (Class<Optional<?>>) this.method.getParameterTypes()[index]);
                continue;
            }

            // bind
            this.paramBinds.put(index, this.method.getParameterTypes()[index]);
        }

        this.path = executor.path();
        this.description = executor.description();

        this.commandExecutor = new CommandExecutor(commandMeta, this);
    }

    public String getUsage(boolean renderJoiningArgs) {

        final List<String> listBuilder = new ArrayList<>();

        if (!this.path.isEmpty()) {
            listBuilder.addAll(Arrays.asList(this.path.split(" ")));
        }

        this.paramAnnotations.forEach((index, annotations) -> {

            if (this.paramArgs.containsKey(index)) {
                listBuilder.add("<" + this.paramNames.get(index) + ">");
                return;
            }

            if (renderJoiningArgs && this.paramMultiArgs.containsKey(index)) {
                listBuilder.add("<" + this.paramNames.get(index) + ">");
                return;
            }

            if (this.paramOptionalArgs.containsKey(index)) {
                listBuilder.add("[" + this.paramNames.get(index) + "]");
            }
        });

        return StringUtil.join(listBuilder, " ");
    }

    public List<String> getSuggestion(@NonNull SuggestionService suggestionService, @NonNull CommandInput commandInput) {
        if (commandInput.getArguments().length == 0) {

            if (!commandInput.isSpaceAtTheEnd() || !this.paramNames.containsKey(0)) {
                return new ArrayList<>();
            }

            final String arg = this.paramNames.get(0);
            final Optional<Completion> optionalCompletion = Arrays.stream(this.method.getAnnotationsByType(Completion.class))
                    .filter(completion -> Objects.equals(completion.arg(), arg))
                    .findAny();

            if (!optionalCompletion.isPresent()) {
                return Collections.singletonList("<" + arg + ">");
            }

            final Completion completion = optionalCompletion.get();
            return suggestionService.getSuggestion(completion);
        }

        final int index = commandInput.getArguments().length - 1;
        final String lastWord = commandInput.getArguments()[index];

        if (!this.paramNames.containsKey(index)) {
            return new ArrayList<>();
        }

        final String arg = this.paramNames.get(index);
        final Optional<Completion> optionalCompletion = Arrays.stream(this.method.getAnnotationsByType(Completion.class))
                .filter(completion -> Objects.equals(completion.arg(), arg))
                .findAny();

        if (!optionalCompletion.isPresent()) {
            return Collections.singletonList("<" + arg + ">");
        }

        final Completion completion = optionalCompletion.get();
        return suggestionService.getSuggestion(completion)
                .stream()
                .filter(text -> text.startsWith(lastWord))
                .collect(Collectors.toList());
    }
}
