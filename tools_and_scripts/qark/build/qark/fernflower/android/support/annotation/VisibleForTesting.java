package android.support.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface VisibleForTesting {
   int NONE = 5;
   int PACKAGE_PRIVATE = 3;
   int PRIVATE = 2;
   int PROTECTED = 4;

   int otherwise() default 2;
}
