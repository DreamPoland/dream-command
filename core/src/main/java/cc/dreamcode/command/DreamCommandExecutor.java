package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Args;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.command.annotation.RequireSender;
import cc.dreamcode.command.bind.BindManager;
import cc.dreamcode.command.context.CommandContext;
import cc.dreamcode.command.context.CommandInvokeContext;
import cc.dreamcode.command.context.CommandPathContext;
import cc.dreamcode.command.exception.CommandException;
import cc.dreamcode.command.extension.ExtensionManager;
import cc.dreamcode.command.handler.HandlerManager;
import cc.dreamcode.command.handler.HandlerType;
import cc.dreamcode.command.handler.type.InvalidInputValueType;
import cc.dreamcode.command.handler.type.InvalidSenderType;
import cc.dreamcode.command.handler.type.InvalidUsageType;
import cc.dreamcode.command.handler.type.NoPermissionType;
import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.command.shared.AnnotationUtil;
import cc.dreamcode.utilities.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Setter
@NoArgsConstructor
public abstract class DreamCommandExecutor {

    private @Getter CommandContext context;
    private ExtensionManager extensionManager;
    private HandlerManager handlerManager;
    private BindManager bindManager;

    public boolean invokeMethod(@NonNull DreamSender<?> sender, @NonNull CommandInvokeContext commandInvokeContext) {
        final RequireSender requireSender = this.getClass().getAnnotation(RequireSender.class);
        if (requireSender != null && !sender.getSenderType().equals(requireSender.type())) {
            this.handlerManager.getCommandHandler(HandlerType.INVALID_SENDER_TYPE).ifPresent(commandHandler -> {
                final InvalidSenderType invalidSenderType = (InvalidSenderType) commandHandler;
                invalidSenderType.handle(sender, requireSender.type());
            });

            return false;
        }

        final Permission permission = this.getClass().getAnnotation(Permission.class);
        if (permission != null && !sender.hasPermission(permission.name())) {
            this.handlerManager.getCommandHandler(HandlerType.NO_PERMISSION).ifPresent(commandHandler -> {
                final NoPermissionType noPermissionType = (NoPermissionType) commandHandler;
                noPermissionType.handle(sender, permission.name());
            });

            return false;
        }

        final DreamCommandValidator validator = new DreamCommandValidator(commandInvokeContext);
        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {

            if (!validator.isValid(this.context, declaredMethod)) {
                continue;
            }

            // scan for path name priority; first path, second @Arg
            if (Arrays.stream(this.getClass().getDeclaredMethods())
                    .filter(method -> !declaredMethod.equals(method))
                    .anyMatch(method -> {
                        final String argument = StringUtil.join(commandInvokeContext.getArguments(), " ").toLowerCase();
                        final String methodPath = method.getAnnotation(Path.class).name().toLowerCase();

                        return argument.equals(methodPath);
                    })) {
                continue;
            }

            final RequireSender methodRequireSender = declaredMethod.getAnnotation(RequireSender.class);
            if (methodRequireSender != null && !sender.getSenderType().equals(methodRequireSender.type())) {
                this.handlerManager.getCommandHandler(HandlerType.INVALID_SENDER_TYPE).ifPresent(commandHandler -> {
                    final InvalidSenderType invalidSenderType = (InvalidSenderType) commandHandler;
                    invalidSenderType.handle(sender, methodRequireSender.type());
                });

                return false;
            }

            final Permission methodPermission = declaredMethod.getAnnotation(Permission.class);
            if (methodPermission != null && !sender.hasPermission(methodPermission.name())) {
                this.handlerManager.getCommandHandler(HandlerType.NO_PERMISSION).ifPresent(commandHandler -> {
                    final NoPermissionType noPermissionType = (NoPermissionType) commandHandler;
                    noPermissionType.handle(sender, methodPermission.name());
                });

                return false;
            }

            final CommandPathContext commandPathContext = new CommandPathContext(this.context, declaredMethod);
            final String usingPath = validator.getUsingPathName(commandPathContext)
                    .orElseThrow(() -> new CommandException("Cannot find using path by context: " + commandInvokeContext));

            final AtomicInteger otherParams = new AtomicInteger();
            final Object[] invokeObjects = new Object[declaredMethod.getParameterCount()];
            final String[] invokeArgs = new String[commandPathContext.getMethodArgs().size() + commandPathContext.getMethodArgsRow().size()];
            try {
                System.arraycopy(commandInvokeContext.getArguments(), usingPath.isEmpty() ? 0 : usingPath.split(" ").length, invokeArgs, 0, invokeArgs.length);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }

            for (int indexRaw = 0; indexRaw < declaredMethod.getParameterCount(); indexRaw++) {
                final Class<?> objectClass = declaredMethod.getParameterTypes()[indexRaw];

                if (declaredMethod.getParameterAnnotations().length > indexRaw) {

                    final Optional<Annotation> optionalArgsAnnotation = Arrays.stream(declaredMethod.getParameterAnnotations()[indexRaw])
                            .filter(annotation -> annotation.annotationType().isAssignableFrom(Args.class))
                            .findAny();

                    if (optionalArgsAnnotation.isPresent()) {
                        final Args args = (Args) optionalArgsAnnotation.get();
                        final int min = args.min() == -1 ? 0 : args.min();
                        final int max = args.max() == -1 ? commandInvokeContext.getArguments().length : args.max();

                        final AtomicReference<String> join = new AtomicReference<>();
                        try {
                            join.set(StringUtil.join(commandInvokeContext.getArguments(), " ", min, max));
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            join.set(StringUtil.join(commandInvokeContext.getArguments(), " ", min, commandInvokeContext.getArguments().length));
                        }

                        try {
                            if (objectClass.isAssignableFrom(String.class)) {
                                invokeObjects[indexRaw] = join.get();
                                continue;
                            }

                            final String[] split = join.get().split(" ");
                            if (objectClass.getComponentType().isAssignableFrom(String.class)) {
                                invokeObjects[indexRaw] = split;
                                continue;
                            }

                            invokeObjects[indexRaw] = Arrays.stream(split)
                                    .map(input -> this.extensionManager.resolveObject(objectClass.getComponentType(), input)
                                            .orElseThrow(() -> new CommandException("Cannot find extension resolver for class " + objectClass.getComponentType())))
                                    .toArray();

                            continue;
                        }
                        catch (IllegalArgumentException e) {
                            final int finalIndexRaw = indexRaw;
                            this.handlerManager.getCommandHandler(HandlerType.INVALID_INPUT_VALUE).ifPresent(commandHandler -> {
                                final InvalidInputValueType invalidInputValueType = (InvalidInputValueType) commandHandler;
                                invalidInputValueType.handle(sender, objectClass.getComponentType(), join.get(), finalIndexRaw);
                            });

                            return false;
                        }
                    }

                    final Optional<Annotation> optionalArgAnnotation = Arrays.stream(declaredMethod.getParameterAnnotations()[indexRaw])
                            .filter(annotation -> annotation.annotationType().isAssignableFrom(Arg.class))
                            .findAny();

                    if (optionalArgAnnotation.isPresent()) {
                        final String input = invokeArgs[indexRaw - otherParams.get()];

                        try {
                            invokeObjects[indexRaw] = this.extensionManager.resolveObject(objectClass, input)
                                    .orElseThrow(() -> new CommandException("Cannot find extension resolver for class " + objectClass.getSimpleName()));

                            continue;
                        }
                        catch (IllegalArgumentException e) {
                            final int finalIndexRaw = indexRaw;
                            this.handlerManager.getCommandHandler(HandlerType.INVALID_INPUT_VALUE).ifPresent(commandHandler -> {
                                final InvalidInputValueType invalidInputValueType = (InvalidInputValueType) commandHandler;
                                invalidInputValueType.handle(sender, objectClass, input, finalIndexRaw);
                            });

                            return false;
                        }
                    }
                }

                final Optional<?> optionalObject = this.bindManager.resolveBind(objectClass, sender);
                if (!optionalObject.isPresent()) {
                    throw new CommandException("Bind with class type " + objectClass.getSimpleName() + " not found");
                }

                invokeObjects[indexRaw] = optionalObject.get();
                otherParams.incrementAndGet();
            }

            try {
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(this, invokeObjects);
                return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new CommandException("Cannot invoke path method", e);
            }
        }

        this.handlerManager.getCommandHandler(HandlerType.INVALID_USAGE).ifPresent(commandHandler -> {
            final InvalidUsageType invalidUsageType = (InvalidUsageType) commandHandler;
            invalidUsageType.handle(sender, this, this.getCommandPathList(), commandInvokeContext);
        });
        return false;
    }

    public List<String> getSuggestion(@NonNull DreamSender<?> sender, @NonNull CommandInvokeContext commandInvokeContext) {
        final List<String> suggestions = new ArrayList<>();

        final RequireSender requireSender = this.getClass().getAnnotation(RequireSender.class);
        if (requireSender != null && !sender.getSenderType().equals(requireSender.type())) {
            return suggestions;
        }

        final Permission permission = this.getClass().getAnnotation(Permission.class);
        if (permission != null && !sender.hasPermission(permission.name())) {
            return suggestions;
        }

        final int lastIndex = commandInvokeContext.getArguments().length - 1;
        if (lastIndex == -1) {
            return suggestions;
        }

        final String lastWord = commandInvokeContext.getArguments()[lastIndex];
        final String[] trimmedInvoke = new String[lastIndex];
        System.arraycopy(commandInvokeContext.getArguments(), 0, trimmedInvoke, 0, trimmedInvoke.length);

        final CommandInvokeContext trimmedInvokeContext = CommandInvokeContext.of(commandInvokeContext.getLabel(), trimmedInvoke);
        final DreamCommandValidator validator = new DreamCommandValidator(trimmedInvokeContext);
        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {

            if (!validator.canBeSuggestion(this.context, declaredMethod)) {
                continue;
            }

            final RequireSender methodRequireSender = declaredMethod.getAnnotation(RequireSender.class);
            if (methodRequireSender != null && !sender.getSenderType().equals(methodRequireSender.type())) {
                return suggestions;
            }

            final Permission methodPermission = declaredMethod.getAnnotation(Permission.class);
            if (methodPermission != null && !sender.hasPermission(methodPermission.name())) {
                return suggestions;
            }

            final CommandPathContext commandPathContext = new CommandPathContext(this.context, declaredMethod);
            for (String pathName : commandPathContext.getPathNameAndAliases()) {
                final String[] pathNameRow = pathName.split(" ");

                if (trimmedInvokeContext.getArguments().length - pathNameRow.length >= commandPathContext.getMethodArgs().size()) {
                    if (!commandPathContext.getMethodArgsRow().isEmpty()) {
                        commandPathContext.getMethodArgsRow().forEach(args -> suggestions.add("<" + args.name() + ">"));
                    }

                    continue;
                }

                if (pathNameRow.length <= trimmedInvokeContext.getArguments().length) {
                    final Class<?> paramClass = declaredMethod.getParameterTypes()[trimmedInvokeContext.getArguments().length - pathNameRow.length + 1];

                    final Optional<List<String>> extensionSuggestion = this.extensionManager.resolveSuggestion(paramClass, lastWord);
                    if (extensionSuggestion.isPresent() && !extensionSuggestion.get().isEmpty()) {
                        suggestions.addAll(extensionSuggestion.get());
                        continue;
                    }

                    final String methodSuggestion = commandPathContext.getMethodArgsNames().get(trimmedInvokeContext.getArguments().length - pathNameRow.length);
                    suggestions.add("<" + methodSuggestion + ">");
                    continue;
                }

                final String suggestion = pathNameRow[trimmedInvokeContext.getArguments().length];
                if (!suggestion.isEmpty()) {
                    suggestions.add(suggestion);
                }
            }
        }

        return suggestions.stream()
                .distinct()
                .filter(text -> {
                    if (text.isEmpty()) {
                        return false;
                    }

                    if (text.startsWith("<") && text.endsWith(">")) {
                        return true;
                    }

                    return text.toLowerCase().startsWith(lastWord.toLowerCase());
                })
                .collect(Collectors.toList());
    }

    public List<CommandPathContext> getCommandPathList() {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.getAnnotation(Path.class) != null)
                .map(method -> {
                    final Path path = method.getAnnotation(Path.class);
                    return new CommandPathContext(
                            this.context,
                            path,
                            AnnotationUtil.getAnnotation(method, Arg.class),
                            AnnotationUtil.getAnnotation(method, Args.class)
                    );
                })
                .collect(Collectors.toList());
    }
}
