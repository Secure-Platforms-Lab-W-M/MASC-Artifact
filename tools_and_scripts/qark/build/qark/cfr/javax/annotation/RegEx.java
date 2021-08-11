/*
 * Decompiled with CFR 0_124.
 */
package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Syntax;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;

@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@Syntax(value="RegEx")
@TypeQualifierNickname
public @interface RegEx {
    public When when() default When.ALWAYS;
}

