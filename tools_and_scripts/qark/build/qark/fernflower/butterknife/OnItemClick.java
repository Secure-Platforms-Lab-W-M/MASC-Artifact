package butterknife;

import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ListenerClass(
   method = {@ListenerMethod(
   name = "onItemClick",
   parameters = {"android.widget.AdapterView<?>", "android.view.View", "int", "long"}
)},
   setter = "setOnItemClickListener",
   targetType = "android.widget.AdapterView<?>",
   type = "android.widget.AdapterView.OnItemClickListener"
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnItemClick {
   int[] value() default {-1};
}
