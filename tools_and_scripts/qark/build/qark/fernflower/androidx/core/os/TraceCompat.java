package androidx.core.os;

import android.os.Trace;
import android.os.Build.VERSION;

public final class TraceCompat {
   private TraceCompat() {
   }

   public static void beginSection(String var0) {
      if (VERSION.SDK_INT >= 18) {
         Trace.beginSection(var0);
      }

   }

   public static void endSection() {
      if (VERSION.SDK_INT >= 18) {
         Trace.endSection();
      }

   }
}
