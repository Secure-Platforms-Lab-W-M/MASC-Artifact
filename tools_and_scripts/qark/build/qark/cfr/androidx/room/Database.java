/*
 * Decompiled with CFR 0_124.
 */
package androidx.room;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.TYPE})
public @interface Database {
    public Class[] entities();

    public boolean exportSchema() default true;

    public int version();

    public Class[] views() default {};
}

