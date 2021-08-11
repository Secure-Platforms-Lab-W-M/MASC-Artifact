package butterknife;

import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ListenerClass(
   callbacks = OnTextChanged.Callback.class,
   remover = "removeTextChangedListener",
   setter = "addTextChangedListener",
   targetType = "android.widget.TextView",
   type = "android.text.TextWatcher"
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnTextChanged {
   OnTextChanged.Callback callback() default OnTextChanged.Callback.TEXT_CHANGED;

   int[] value() default {-1};

   public static enum Callback {
      @ListenerMethod(
         name = "afterTextChanged",
         parameters = {"android.text.Editable"}
      )
      AFTER_TEXT_CHANGED,
      @ListenerMethod(
         name = "beforeTextChanged",
         parameters = {"java.lang.CharSequence", "int", "int", "int"}
      )
      BEFORE_TEXT_CHANGED,
      @ListenerMethod(
         name = "onTextChanged",
         parameters = {"java.lang.CharSequence", "int", "int", "int"}
      )
      TEXT_CHANGED;

      static {
         OnTextChanged.Callback var0 = new OnTextChanged.Callback("AFTER_TEXT_CHANGED", 2);
         AFTER_TEXT_CHANGED = var0;
      }
   }
}
