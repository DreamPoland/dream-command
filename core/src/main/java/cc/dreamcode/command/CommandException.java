package cc.dreamcode.command;


import cc.dreamcode.notice.Notice;
import lombok.Getter;
import lombok.NonNull;

@SuppressWarnings("rawtypes")
public final class CommandException extends RuntimeException {

    @Getter
    private final Notice notice;

    public CommandException(@NonNull Notice notice) {
        this.notice = notice;
    }
}
