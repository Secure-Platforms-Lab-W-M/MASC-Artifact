package android.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE})
public @interface FloatRange {
   double from() default Double.NEGATIVE_INFINITY;

   boolean fromInclusive() default true;

   // $FF: renamed from: to () double
   double method_1() default Double.POSITIVE_INFINITY;

   boolean toInclusive() default true;
}
