package butterknife.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface ListenerClass {
   Class callbacks() default ListenerClass.NONE.class;

   ListenerMethod[] method() default {};

   String remover() default "";

   String setter();

   String targetType();

   String type();

   public static enum NONE {
   }
}
