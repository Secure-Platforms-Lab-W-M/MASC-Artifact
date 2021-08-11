package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.TypeQualifierValidator;
import javax.annotation.meta.When;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifier(
   applicableTo = Number.class
)
public @interface Nonnegative {
   When when() default When.ALWAYS;

   public static class Checker implements TypeQualifierValidator {
      public When forConstantValue(Nonnegative var1, Object var2) {
         if (!(var2 instanceof Number)) {
            return When.NEVER;
         } else {
            Number var8 = (Number)var2;
            boolean var7 = var8 instanceof Long;
            boolean var4 = true;
            boolean var5 = true;
            boolean var6 = true;
            boolean var3 = true;
            if (var7) {
               if (var8.longValue() >= 0L) {
                  var3 = false;
               }
            } else if (var8 instanceof Double) {
               if (var8.doubleValue() < 0.0D) {
                  var3 = var4;
               } else {
                  var3 = false;
               }
            } else if (var8 instanceof Float) {
               if (var8.floatValue() < 0.0F) {
                  var3 = var5;
               } else {
                  var3 = false;
               }
            } else if (var8.intValue() < 0) {
               var3 = var6;
            } else {
               var3 = false;
            }

            return var3 ? When.NEVER : When.ALWAYS;
         }
      }
   }
}
