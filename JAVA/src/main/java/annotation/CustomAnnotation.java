package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAnnotation {
    String value() default "CustomAnnotation"; // default param
    String data() default "defaultData"; // custom param
}