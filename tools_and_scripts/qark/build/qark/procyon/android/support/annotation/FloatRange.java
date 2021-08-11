// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
public @interface FloatRange {
    double from() default Double.NEGATIVE_INFINITY;
    
    boolean fromInclusive() default true;
    
    double to() default Double.POSITIVE_INFINITY;
    
    boolean toInclusive() default true;
}
