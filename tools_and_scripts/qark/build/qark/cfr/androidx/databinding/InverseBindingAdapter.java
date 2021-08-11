/*
 * Decompiled with CFR 0_124.
 */
package androidx.databinding;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value={ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface InverseBindingAdapter {
    public String attribute();

    public String event() default "";
}

