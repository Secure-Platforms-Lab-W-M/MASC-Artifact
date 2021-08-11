package androidx.core.text;

import android.icu.util.ULocale;
import android.os.Build.VERSION;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public final class ICUCompat {
   private static final String TAG = "ICUCompat";
   private static Method sAddLikelySubtagsMethod;
   private static Method sGetScriptMethod;

   static {
      if (VERSION.SDK_INT < 21) {
         label33: {
            Exception var10000;
            label38: {
               Class var0;
               boolean var10001;
               try {
                  var0 = Class.forName("libcore.icu.ICU");
               } catch (Exception var3) {
                  var10000 = var3;
                  var10001 = false;
                  break label38;
               }

               if (var0 == null) {
                  break label33;
               }

               try {
                  sGetScriptMethod = var0.getMethod("getScript", String.class);
                  sAddLikelySubtagsMethod = var0.getMethod("addLikelySubtags", String.class);
                  break label33;
               } catch (Exception var2) {
                  var10000 = var2;
                  var10001 = false;
               }
            }

            Exception var4 = var10000;
            sGetScriptMethod = null;
            sAddLikelySubtagsMethod = null;
            Log.w("ICUCompat", var4);
         }

      } else if (VERSION.SDK_INT < 24) {
         try {
            sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
         } catch (Exception var1) {
            throw new IllegalStateException(var1);
         }
      }
   }

   private ICUCompat() {
   }

   private static String addLikelySubtags(Locale var0) {
      String var4 = var0.toString();

      try {
         if (sAddLikelySubtagsMethod != null) {
            String var1 = (String)sAddLikelySubtagsMethod.invoke((Object)null, var4);
            return var1;
         }
      } catch (IllegalAccessException var2) {
         Log.w("ICUCompat", var2);
      } catch (InvocationTargetException var3) {
         Log.w("ICUCompat", var3);
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
         Log.w("ICUCompat", var1);
      } catch (InvocationTargetException var2) {
         Log.w("ICUCompat", var2);
         return null;
      }

      return null;
   }

   public static String maximizeAndGetScript(Locale var0) {
      if (VERSION.SDK_INT >= 24) {
         return ULocale.addLikelySubtags(ULocale.forLocale(var0)).getScript();
      } else if (VERSION.SDK_INT >= 21) {
         try {
            String var1 = ((Locale)sAddLikelySubtagsMethod.invoke((Object)null, var0)).getScript();
            return var1;
         } catch (InvocationTargetException var2) {
            Log.w("ICUCompat", var2);
         } catch (IllegalAccessException var3) {
            Log.w("ICUCompat", var3);
         }

         return var0.getScript();
      } else {
         String var4 = addLikelySubtags(var0);
         return var4 != null ? getScript(var4) : null;
      }
   }
}
