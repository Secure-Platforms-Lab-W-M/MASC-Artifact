/*
 * Decompiled with CFR 0_124.
 */
package androidx.databinding;

import androidx.databinding.InverseBindingMethod;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value={ElementType.TYPE})
public @interface InverseBindingMethods {
    public InverseBindingMethod[] value();
}

