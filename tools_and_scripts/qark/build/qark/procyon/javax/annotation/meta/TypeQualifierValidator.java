// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package javax.annotation.meta;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

public interface TypeQualifierValidator<A extends Annotation>
{
    @Nonnull
    When forConstantValue(@Nonnull final A p0, final Object p1);
}
