package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import java.io.File;

@TargetApi(19)
@RequiresApi(19)
class ContextCompatKitKat {
   public static File[] getExternalCacheDirs(Context var0) {
      return var0.getExternalCacheDirs();
   }

   public static File[] getExternalFilesDirs(Context var0, String var1) {
      return var0.getExternalFilesDirs(var1);
   }

   public static File[] getObbDirs(Context var0) {
      return var0.getObbDirs();
   }
}
