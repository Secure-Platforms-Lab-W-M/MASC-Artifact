/*
 * Decompiled with CFR 0_124.
 */
package androidx.room;

import java.lang.annotation.Annotation;

public @interface Update {
    public int onConflict() default 3;
}

