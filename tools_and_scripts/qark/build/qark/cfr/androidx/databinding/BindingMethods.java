/*
 * Decompiled with CFR 0_124.
 */
package androidx.databinding;

import androidx.databinding.BindingMethod;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value={ElementType.TYPE})
public @interface BindingMethods {
    public BindingMethod[] value();
}

