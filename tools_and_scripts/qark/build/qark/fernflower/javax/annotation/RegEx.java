package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Syntax("RegEx")
public @interface RegEx {
   When when() default When.ALWAYS;

   public static class Checker implements TypeQualifierValidator {
      public When forConstantValue(RegEx var1, Object var2) {
         if (!(var2 instanceof String)) {
            return When.NEVER;
         } else {
            try {
               Pattern.compile((String)var2);
            } catch (PatternSyntaxException var3) {
               return When.NEVER;
            }

            return When.ALWAYS;
         }
      }
   }
}
