/*
 * Decompiled with CFR 0_124.
 */
package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nullable;
import javax.annotation.meta.TypeQualifierDefault;

@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@Nullable
@TypeQualifierDefault(value={ElementType.PARAMETER})
public @interface ParametersAreNullableByDefault {
}

