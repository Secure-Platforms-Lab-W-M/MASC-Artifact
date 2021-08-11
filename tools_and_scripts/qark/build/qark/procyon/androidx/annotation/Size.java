// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface Size {
    long max() default Long.MAX_VALUE;
    
    long min() default Long.MIN_VALUE;
    
    long multiple() default 1L;
    
    long value() default -1L;
}
