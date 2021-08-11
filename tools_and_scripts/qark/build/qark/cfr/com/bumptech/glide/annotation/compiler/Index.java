/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.annotation.compiler;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.TYPE})
@interface Index {
    public String[] extensions() default {};

    public String[] modules() default {};
}

