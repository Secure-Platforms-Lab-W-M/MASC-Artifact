package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifier
public @interface Nonnull {
   When when() default When.ALWAYS;

   public static class Checker implements TypeQualifierValidator {
      public When forConstantValue(Nonnull var1, Object var2) {
         return var2 == null ? When.NEVER : When.ALWAYS;
      }
   }
}
