// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface StringDef {
    String[] value() default {};
}
