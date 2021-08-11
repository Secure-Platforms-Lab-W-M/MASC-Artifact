// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.room;

import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.CLASS)
@Target({})
public @interface Index {
    String name() default "";
    
    boolean unique() default false;
    
    String[] value();
}
