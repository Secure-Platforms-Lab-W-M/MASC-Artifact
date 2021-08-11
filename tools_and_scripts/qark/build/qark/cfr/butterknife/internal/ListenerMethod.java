/*
 * Decompiled with CFR 0_124.
 */
package butterknife.internal;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface ListenerMethod {
    public String defaultReturn() default "null";

    public String name();

    public String[] parameters() default {};

    public String returnType() default "void";
}

