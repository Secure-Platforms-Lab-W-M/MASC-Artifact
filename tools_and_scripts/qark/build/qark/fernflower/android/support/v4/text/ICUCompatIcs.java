package android.support.v4.text;

import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

@RequiresApi(14)
class ICUCompatIcs {
   private static final String TAG = "ICUCompatIcs";
   private static Method sAddLikelySubtagsMethod;
   private static Method sGetScriptMethod;

   static {
      // $FF: Couldn't be decompiled
   }

   private static String addLikelySubtags(Locale var0) {
      String var4 = var0.toString();

      try {
         if (sAddLikelySubtagsMethod != null) {
            String var1 = (String)sAddLikelySubtagsMethod.invoke((Object)null, var4);
            return var1;
         }
      } catch (IllegalAccessException var2) {
         Log.w("ICUCompatIcs", var2);
      } catch (InvocationTargetException var3) {
         Log.w("ICUCompatIcs", var3);
         return var4;
      }

      return var4;
   }

   private static String getScript(String var0) {
      try {
         if (sGetScriptMethod != null) {
            var0 = (String)sGetScriptMethod.invoke((Object)null, var0);
            return var0;
         }
      } catch (IllegalAccessException var1) {
         Log.w("ICUCompatIcs", var1);
      } catch (InvocationTargetException var2) {
         Log.w("ICUCompatIcs", var2);
         return null;
      }

      return null;
   }

   public static String maximizeAndGetScript(Locale var0) {
      String var1 = addLikelySubtags(var0);
      return var1 != null ? getScript(var1) : null;
   }
}
