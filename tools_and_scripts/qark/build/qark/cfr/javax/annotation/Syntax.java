/*
 * Decompiled with CFR 0_124.
 */
package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.When;

@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@TypeQualifier(applicableTo=CharSequence.class)
public @interface Syntax {
    public String value();

    public When when() default When.ALWAYS;
}

