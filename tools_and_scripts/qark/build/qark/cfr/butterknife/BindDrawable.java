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
public @interface BindDrawable {
    public int tint() default -1;

    public int value();
}

