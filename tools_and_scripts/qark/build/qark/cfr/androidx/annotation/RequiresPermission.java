/*
 * Decompiled with CFR 0_124.
 */
package androidx.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PARAMETER})
public @interface RequiresPermission {
    public String[] allOf() default {};

    public String[] anyOf() default {};

    public boolean conditional() default false;

    public String value() default "";

    @Target(value={ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
    public static @interface Read {
        public RequiresPermission value() default @RequiresPermission;
    }

    @Target(value={ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
    public static @interface Write {
        public RequiresPermission value() default @RequiresPermission;
    }

}

