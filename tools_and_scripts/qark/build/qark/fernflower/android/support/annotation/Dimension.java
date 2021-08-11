package android.support.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE})
public @interface Dimension {
   // $FF: renamed from: DP int
   int field_0 = 0;
   // $FF: renamed from: PX int
   int field_1 = 1;
   // $FF: renamed from: SP int
   int field_2 = 2;

   int unit() default 1;
}
