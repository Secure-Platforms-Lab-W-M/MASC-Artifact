/*
 * Decompiled with CFR 0_124.
 */
package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface OverridingMethodsMustInvokeSuper {
}

