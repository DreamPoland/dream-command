package cc.dreamcode.command;

import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.utilities.StringUtil;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public interface DreamCommandExecutor {

    default void invokeMethod(@NonNull String pathText) {
        final String[] args = pathText.split(" ");
        Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.getAnnotation(Path.class) != null)
                .filter(method -> {
                    final Path path = method.getAnnotation(Path.class);
                    final String prefix = StringUtil.join(args, " ", 1, args.length);
                    if (!path.name().startsWith(prefix) && Arrays.stream(path.aliases())
                            .noneMatch(text -> text.startsWith(prefix))) {
                        return false;
                    }

                    method.setAccessible(true);
                    return true;
                })
                .findAny()
                .ifPresent(method -> {
                    try {
                        method.invoke(this);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new DreamCommandException("Cannot invoke path method", e);
                    }
                });
    }
}
