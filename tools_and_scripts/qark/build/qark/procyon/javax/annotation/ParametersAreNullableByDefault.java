// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package javax.annotation;

import java.lang.annotation.ElementType;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Nullable
@TypeQualifierDefault({ ElementType.PARAMETER })
public @interface ParametersAreNullableByDefault {
}
