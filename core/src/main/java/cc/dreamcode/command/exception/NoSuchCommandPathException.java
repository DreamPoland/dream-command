package cc.dreamcode.command.exception;

import cc.dreamcode.command.path.CommandPath;
import lombok.NonNull;

public class NoSuchCommandPathException extends RuntimeException {

    public NoSuchCommandPathException(@NonNull CommandPath commandPath) {
        super("Cannot find any method with path: " + commandPath + " or default usage fallback.");
    }
}
