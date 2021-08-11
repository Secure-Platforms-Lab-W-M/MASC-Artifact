package androidx.core.database;

import android.database.CursorWindow;
import android.os.Build.VERSION;

public final class CursorWindowCompat {
   private CursorWindowCompat() {
   }

   public static CursorWindow create(String var0, long var1) {
      if (VERSION.SDK_INT >= 28) {
         return new CursorWindow(var0, var1);
      } else {
         return VERSION.SDK_INT >= 15 ? new CursorWindow(var0) : new CursorWindow(false);
      }
   }
}
