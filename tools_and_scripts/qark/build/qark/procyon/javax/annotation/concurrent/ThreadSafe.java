// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package javax.annotation.concurrent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE })
public @interface ThreadSafe {
}
