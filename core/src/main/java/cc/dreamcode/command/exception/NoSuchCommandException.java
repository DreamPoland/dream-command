package cc.dreamcode.command.exception;

import cc.dreamcode.command.path.CommandPath;
import lombok.NonNull;

public class NoSuchCommandException extends RuntimeException {

    public NoSuchCommandException(@NonNull CommandPath commandPath) {
        super("Cannot find any command with path: " + commandPath);
    }
}
