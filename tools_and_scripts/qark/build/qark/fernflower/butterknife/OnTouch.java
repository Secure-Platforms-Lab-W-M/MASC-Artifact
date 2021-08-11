package butterknife;

import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ListenerClass(
   method = {@ListenerMethod(
   defaultReturn = "true",
   name = "onTouch",
   parameters = {"android.view.View", "android.view.MotionEvent"},
   returnType = "boolean"
)},
   setter = "setOnTouchListener",
   targetType = "android.view.View",
   type = "android.view.View.OnTouchListener"
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnTouch {
   int[] value() default {-1};
}
