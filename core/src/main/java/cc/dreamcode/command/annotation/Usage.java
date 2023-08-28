package cc.dreamcode.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Usage {

    /**
     * Zero value designates the default method for all command syntax.
     */
    int arg() default 0;
}
