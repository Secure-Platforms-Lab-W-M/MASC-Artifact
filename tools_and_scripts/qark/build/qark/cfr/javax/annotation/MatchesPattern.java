/*
 * Decompiled with CFR 0_124.
 */
package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.RegEx;
import javax.annotation.meta.TypeQualifier;

@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@TypeQualifier(applicableTo=String.class)
public @interface MatchesPattern {
    public int flags() default 0;

    @RegEx
    public String value();
}

