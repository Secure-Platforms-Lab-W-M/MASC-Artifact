package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifier(
   applicableTo = String.class
)
public @interface MatchesPattern {
   int flags() default 0;

   @RegEx
   String value();

   public static class Checker implements TypeQualifierValidator {
      public When forConstantValue(MatchesPattern var1, Object var2) {
         return Pattern.compile(var1.value(), var1.flags()).matcher((String)var2).matches() ? When.ALWAYS : When.NEVER;
      }
   }
}
