package cc.dreamcode.command.shared;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

@UtilityClass
public class AnnotationUtil {
    public static long countAnnotation(@NonNull Method method, @NonNull Class<? extends Annotation> annotationClass) {
        return Arrays.stream(method.getParameterAnnotations())
                .filter(annotations -> Arrays.stream(annotations)
                        .anyMatch(annotation -> annotation.annotationType().equals(annotationClass)))
                .count();
    }
}
