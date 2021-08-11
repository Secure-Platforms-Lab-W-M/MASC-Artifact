/*
 * Decompiled with CFR 0_124.
 */
package androidx.versionedparcelable;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.SOURCE)
@Target(value={ElementType.TYPE})
public @interface VersionedParcelize {
    public boolean allowSerialization() default false;

    public int[] deprecatedIds() default {};

    public Class factory() default void.class;

    public boolean ignoreParcelables() default false;

    public boolean isCustom() default false;

    public String jetifyAs() default "";
}

