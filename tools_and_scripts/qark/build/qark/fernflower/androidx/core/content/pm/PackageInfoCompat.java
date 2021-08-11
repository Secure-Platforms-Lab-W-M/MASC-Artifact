package androidx.core.content.pm;

import android.content.pm.PackageInfo;
import android.os.Build.VERSION;

public final class PackageInfoCompat {
   private PackageInfoCompat() {
   }

   public static long getLongVersionCode(PackageInfo var0) {
      return VERSION.SDK_INT >= 28 ? var0.getLongVersionCode() : (long)var0.versionCode;
   }
}
