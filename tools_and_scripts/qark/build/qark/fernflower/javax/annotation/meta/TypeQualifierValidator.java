package javax.annotation.meta;

import java.lang.annotation.Annotation;
import javax.annotation.Nonnull;

public interface TypeQualifierValidator {
   @Nonnull
   When forConstantValue(@Nonnull Annotation var1, Object var2);
}
