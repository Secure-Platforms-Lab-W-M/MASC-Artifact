package androidx.core.content.pm;

import android.content.pm.PermissionInfo;
import android.os.Build.VERSION;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PermissionInfoCompat {
   private PermissionInfoCompat() {
   }

   public static int getProtection(PermissionInfo var0) {
      return VERSION.SDK_INT >= 28 ? var0.getProtection() : var0.protectionLevel & 15;
   }

   public static int getProtectionFlags(PermissionInfo var0) {
      return VERSION.SDK_INT >= 28 ? var0.getProtectionFlags() : var0.protectionLevel & -16;
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface Protection {
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface ProtectionFlags {
   }
}
