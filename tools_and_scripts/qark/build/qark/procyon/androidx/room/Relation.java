// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.room;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Relation {
    Class entity() default Object.class;
    
    String entityColumn();
    
    String parentColumn();
    
    String[] projection() default {};
}
