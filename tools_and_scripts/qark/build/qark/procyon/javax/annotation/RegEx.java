// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package javax.annotation;

import javax.annotation.meta.When;
import javax.annotation.meta.TypeQualifierNickname;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Syntax("RegEx")
@TypeQualifierNickname
public @interface RegEx {
    When when() default When.ALWAYS;
}
