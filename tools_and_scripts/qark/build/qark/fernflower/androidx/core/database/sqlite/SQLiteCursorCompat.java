package androidx.core.database.sqlite;

import android.database.sqlite.SQLiteCursor;
import android.os.Build.VERSION;

public final class SQLiteCursorCompat {
   private SQLiteCursorCompat() {
   }

   public static void setFillWindowForwardOnly(SQLiteCursor var0, boolean var1) {
      if (VERSION.SDK_INT >= 28) {
         var0.setFillWindowForwardOnly(var1);
      }

   }
}
