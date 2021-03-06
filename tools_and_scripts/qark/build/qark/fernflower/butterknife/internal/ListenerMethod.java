package butterknife.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ListenerMethod {
   String defaultReturn() default "null";

   String name();

   String[] parameters() default {};

   String returnType() default "void";
}
