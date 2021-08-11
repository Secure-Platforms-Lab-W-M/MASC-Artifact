package butterknife;

import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ListenerClass(
   callbacks = OnPageChange.Callback.class,
   remover = "removeOnPageChangeListener",
   setter = "addOnPageChangeListener",
   targetType = "androidx.viewpager.widget.ViewPager",
   type = "androidx.viewpager.widget.ViewPager.OnPageChangeListener"
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OnPageChange {
   OnPageChange.Callback callback() default OnPageChange.Callback.PAGE_SELECTED;

   int[] value() default {-1};

   public static enum Callback {
      @ListenerMethod(
         name = "onPageScrolled",
         parameters = {"int", "float", "int"}
      )
      PAGE_SCROLLED,
      @ListenerMethod(
         name = "onPageScrollStateChanged",
         parameters = {"int"}
      )
      PAGE_SCROLL_STATE_CHANGED,
      @ListenerMethod(
         name = "onPageSelected",
         parameters = {"int"}
      )
      PAGE_SELECTED;

      static {
         OnPageChange.Callback var0 = new OnPageChange.Callback("PAGE_SCROLL_STATE_CHANGED", 2);
         PAGE_SCROLL_STATE_CHANGED = var0;
      }
   }
}
