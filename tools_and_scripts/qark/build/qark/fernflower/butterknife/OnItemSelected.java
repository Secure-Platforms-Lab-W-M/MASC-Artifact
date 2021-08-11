package butterknife;

import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ListenerClass(
   callbacks = OnItemSelected.Callback.class,
   setter = "setOnItemSelectedListener",
   targetType = "android.widget.AdapterView<?>",
   type = "android.widget.AdapterView.OnItemSelectedListener"
)
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface OnItemSelected {
   OnItemSelected.Callback callback() default OnItemSelected.Callback.ITEM_SELECTED;

   int[] value() default {-1};

   public static enum Callback {
      @ListenerMethod(
         name = "onItemSelected",
         parameters = {"android.widget.AdapterView<?>", "android.view.View", "int", "long"}
      )
      ITEM_SELECTED,
      @ListenerMethod(
         name = "onNothingSelected",
         parameters = {"android.widget.AdapterView<?>"}
      )
      NOTHING_SELECTED;

      static {
         OnItemSelected.Callback var0 = new OnItemSelected.Callback("NOTHING_SELECTED", 1);
         NOTHING_SELECTED = var0;
      }
   }
}
