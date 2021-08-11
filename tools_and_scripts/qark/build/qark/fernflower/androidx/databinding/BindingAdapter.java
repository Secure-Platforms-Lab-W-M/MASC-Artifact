package androidx.databinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
public @interface BindingAdapter {
   boolean requireAll() default true;

   String[] value();
}
