package android.support.v4.text;

import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

@RequiresApi(21)
class ICUCompatApi21 {
   private static final String TAG = "ICUCompatApi21";
   private static Method sAddLikelySubtagsMethod;

   static {
      try {
         sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
      } catch (Exception var1) {
         throw new IllegalStateException(var1);
      }
   }

   public static String maximizeAndGetScript(Locale var0) {
      try {
         String var1 = ((Locale)sAddLikelySubtagsMethod.invoke((Object)null, var0)).getScript();
         return var1;
      } catch (InvocationTargetException var2) {
         Log.w("ICUCompatApi21", var2);
      } catch (IllegalAccessException var3) {
         Log.w("ICUCompatApi21", var3);
      }

      return var0.getScript();
   }
}
