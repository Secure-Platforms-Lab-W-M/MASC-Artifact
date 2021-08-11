package android.support.v4.content;

import android.content.Context;
import java.io.File;

class ContextCompatFroyo {
   public static File getExternalCacheDir(Context var0) {
      return var0.getExternalCacheDir();
   }

   public static File getExternalFilesDir(Context var0, String var1) {
      return var0.getExternalFilesDir(var1);
   }
}
