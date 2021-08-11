/*
 * Decompiled with CFR 0_124.
 */
package butterknife;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface BindFont {
    public int style() default 0;

    public int value();

    public static @interface TypefaceStyle {
    }

}

