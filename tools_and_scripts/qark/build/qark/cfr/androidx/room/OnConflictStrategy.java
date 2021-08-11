/*
 * Decompiled with CFR 0_124.
 */
package androidx.room;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.SOURCE)
public @interface OnConflictStrategy {
    public static final int ABORT = 3;
    public static final int FAIL = 4;
    public static final int IGNORE = 5;
    public static final int REPLACE = 1;
    public static final int ROLLBACK = 2;
}

