
import java.lang.annotation.*;


@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Page {
    String path() default "";
    boolean SerialData() default false;

}
