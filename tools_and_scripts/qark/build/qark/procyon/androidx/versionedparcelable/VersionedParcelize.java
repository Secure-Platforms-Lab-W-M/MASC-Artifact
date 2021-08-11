// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.versionedparcelable;

import androidx.annotation.RestrictTo;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE })
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public @interface VersionedParcelize {
    boolean allowSerialization() default false;
    
    int[] deprecatedIds() default {};
    
    boolean ignoreParcelables() default false;
    
    boolean isCustom() default false;
    
    String jetifyAs() default "";
}
