/*
 * Decompiled with CFR 0_124.
 */
package androidx.databinding;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value={ElementType.ANNOTATION_TYPE})
public @interface BindingMethod {
    public String attribute();

    public String method();

    public Class type();
}

