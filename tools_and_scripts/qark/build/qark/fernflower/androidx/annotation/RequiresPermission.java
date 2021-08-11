package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PARAMETER})
public @interface RequiresPermission {
   String[] allOf() default {};

   String[] anyOf() default {};

   boolean conditional() default false;

   String value() default "";

   @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
   public @interface Read {
      RequiresPermission value() default @RequiresPermission;
   }

   @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
   public @interface Write {
      RequiresPermission value() default @RequiresPermission;
   }
}
