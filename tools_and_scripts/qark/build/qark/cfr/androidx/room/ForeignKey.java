/*
 * Decompiled with CFR 0_124.
 */
package androidx.room;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public @interface ForeignKey {
    public static final int CASCADE = 5;
    public static final int NO_ACTION = 1;
    public static final int RESTRICT = 2;
    public static final int SET_DEFAULT = 4;
    public static final int SET_NULL = 3;

    public String[] childColumns();

    public boolean deferred() default false;

    public Class entity();

    public int onDelete() default 1;

    public int onUpdate() default 1;

    public String[] parentColumns();

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Action {
    }

}

