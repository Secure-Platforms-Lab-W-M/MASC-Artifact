package androidx.core.telephony.mbms;

import android.content.Context;
import android.os.LocaleList;
import android.os.Build.VERSION;
import android.telephony.mbms.ServiceInfo;
import java.util.Iterator;
import java.util.Locale;

public final class MbmsHelper {
   private MbmsHelper() {
   }

   public static CharSequence getBestNameForService(Context var0, ServiceInfo var1) {
      if (VERSION.SDK_INT < 28) {
         return null;
      } else {
         LocaleList var5 = var0.getResources().getConfiguration().getLocales();
         int var2 = var1.getNamedContentLocales().size();
         if (var2 == 0) {
            return null;
         } else {
            String[] var3 = new String[var2];
            var2 = 0;

            for(Iterator var4 = var1.getNamedContentLocales().iterator(); var4.hasNext(); ++var2) {
               var3[var2] = ((Locale)var4.next()).toLanguageTag();
            }

            Locale var6 = var5.getFirstMatch(var3);
            return var6 == null ? null : var1.getNameForLocale(var6);
         }
      }
   }
}
