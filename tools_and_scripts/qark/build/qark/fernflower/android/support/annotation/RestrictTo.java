package android.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
public @interface RestrictTo {
   RestrictTo.Scope[] value();

   public static enum Scope {
      @Deprecated
      GROUP_ID,
      LIBRARY,
      LIBRARY_GROUP,
      SUBCLASSES,
      TESTS;
   }
}
