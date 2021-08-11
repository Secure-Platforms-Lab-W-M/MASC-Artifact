/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.METHOD})
public @interface GlideOption {
    public static final int OVERRIDE_EXTEND = 1;
    public static final int OVERRIDE_NONE = 0;
    public static final int OVERRIDE_REPLACE = 2;

    public boolean memoizeStaticMethod() default false;

    public int override() default 0;

    public boolean skipStaticMethod() default false;

    public String staticMethodName() default "";
}

