/*
 * Decompiled with CFR 0_124.
 */
package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nonnegative;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;

@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@Nonnegative(when=When.MAYBE)
@TypeQualifierNickname
public @interface CheckForSigned {
}

