package cc.dreamcode.command.shared;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class AnnotationUtil {
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> List<T> getAnnotation(@NonNull Method method, @NonNull Class<T> annotationClass) {
        final List<T> annotationList = new ArrayList<>();

        for (Annotation[] parameterAnnotation : method.getParameterAnnotations()) {
            Arrays.stream(parameterAnnotation)
                    .filter(annotation -> annotation.annotationType().equals(annotationClass))
                    .findAny()
                    .ifPresent(annotation -> annotationList.add((T) annotation));
        }

        return annotationList;
    }
}
