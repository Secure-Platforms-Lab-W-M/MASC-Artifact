/*
 * Decompiled with CFR 0_124.
 */
package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface OnLifecycleEvent {
    public Lifecycle.Event value();
}

