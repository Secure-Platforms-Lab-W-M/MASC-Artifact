package android.support.v4.os;

import android.content.res.Configuration;
import android.os.Build.VERSION;

public final class ConfigurationCompat {
   private ConfigurationCompat() {
   }

   public static LocaleListCompat getLocales(Configuration var0) {
      return VERSION.SDK_INT >= 24 ? LocaleListCompat.wrap(var0.getLocales()) : LocaleListCompat.create(var0.locale);
   }
}
