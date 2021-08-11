package androidx.core.graphics;

import android.graphics.Typeface;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TypefaceCompatApi28Impl extends TypefaceCompatApi26Impl {
   private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
   private static final String DEFAULT_FAMILY = "sans-serif";
   private static final int RESOLVE_BY_FONT_TABLE = -1;

   protected Typeface createFromFamiliesWithDefault(Object var1) {
      try {
         Object var2 = Array.newInstance(this.mFontFamily, 1);
         Array.set(var2, 0, var1);
         Typeface var5 = (Typeface)this.mCreateFromFamiliesWithDefault.invoke((Object)null, var2, "sans-serif", -1, -1);
         return var5;
      } catch (IllegalAccessException var3) {
         var1 = var3;
      } catch (InvocationTargetException var4) {
         var1 = var4;
      }

      throw new RuntimeException((Throwable)var1);
   }

   protected Method obtainCreateFromFamiliesWithDefaultMethod(Class var1) throws NoSuchMethodException {
      Method var2 = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(var1, 1).getClass(), String.class, Integer.TYPE, Integer.TYPE);
      var2.setAccessible(true);
      return var2;
   }
}
