/*
 * Decompiled with CFR 0_124.
 */
package androidx.databinding;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value={ElementType.ANNOTATION_TYPE})
public @interface InverseBindingMethod {
    public String attribute();

    public String event() default "";

    public String method() default "";

    public Class type();
}

