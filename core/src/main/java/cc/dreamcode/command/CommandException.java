package cc.dreamcode.command;


import lombok.Getter;
import lombok.NonNull;

public final class CommandException extends RuntimeException {

    @Getter
    private final String notice;

    public CommandException(@NonNull String notice) {
        this.notice = notice;
    }
}
