package cc.dreamcode.command.shared;

import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Args;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class AnnotationUtil {
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> List<T> getAnnotations(@NonNull Method method, @NonNull Class<T> annotationClass) {
        final List<T> annotationList = new ArrayList<>();

        for (Annotation[] parameterAnnotation : method.getParameterAnnotations()) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation.annotationType().equals(annotationClass)) {
                    annotationList.add((T) annotation);
                }
            }
        }

        return annotationList;
    }

    public static Map<Integer, String> getArgNamesByMethod(@NonNull Method method) {
        final Map<Integer, String> argNames = new HashMap<>();

        for (Annotation[] parameterAnnotation : method.getParameterAnnotations()) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation.annotationType().equals(Arg.class)) {
                    final Arg arg = (Arg) annotation;
                    argNames.put(argNames.size(), arg.name());

                    continue;
                }

                if (annotation.annotationType().equals(Args.class)) {
                    final Args args = (Args) annotation;
                    argNames.put(argNames.size(), args.name());
                }
            }
        }

        return argNames;
    }
}
