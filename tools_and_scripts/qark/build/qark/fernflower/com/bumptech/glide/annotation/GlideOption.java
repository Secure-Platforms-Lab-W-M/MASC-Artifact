package com.bumptech.glide.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface GlideOption {
   int OVERRIDE_EXTEND = 1;
   int OVERRIDE_NONE = 0;
   int OVERRIDE_REPLACE = 2;

   boolean memoizeStaticMethod() default false;

   int override() default 0;

   boolean skipStaticMethod() default false;

   String staticMethodName() default "";
}
