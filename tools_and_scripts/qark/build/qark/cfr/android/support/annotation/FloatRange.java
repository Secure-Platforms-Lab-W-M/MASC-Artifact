/*
 * Decompiled with CFR 0_124.
 */
package android.support.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE})
public @interface FloatRange {
    public double from() default Double.NEGATIVE_INFINITY;

    public boolean fromInclusive() default true;

    public double to() default Double.POSITIVE_INFINITY;

    public boolean toInclusive() default true;
}

