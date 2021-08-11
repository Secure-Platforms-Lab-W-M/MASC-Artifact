// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.METHOD })
public @interface InspectableProperty {
    int attributeId() default 0;
    
    EnumEntry[] enumMapping() default {};
    
    FlagEntry[] flagMapping() default {};
    
    boolean hasAttributeId() default true;
    
    String name() default "";
    
    ValueType valueType() default ValueType.INFERRED;
    
    @Retention(RetentionPolicy.SOURCE)
    @Target({ ElementType.TYPE })
    public @interface EnumEntry {
        String name();
        
        int value();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @Target({ ElementType.TYPE })
    public @interface FlagEntry {
        int mask() default 0;
        
        String name();
        
        int target();
    }
    
    public enum ValueType
    {
        COLOR, 
        GRAVITY, 
        INFERRED, 
        INT_ENUM, 
        INT_FLAG, 
        NONE, 
        RESOURCE_ID;
    }
}
