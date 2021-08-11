package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import java.io.File;

@TargetApi(21)
@RequiresApi(21)
class ContextCompatApi21 {
   public static File getCodeCacheDir(Context var0) {
      return var0.getCodeCacheDir();
   }

   public static Drawable getDrawable(Context var0, int var1) {
      return var0.getDrawable(var1);
   }

   public static File getNoBackupFilesDir(Context var0) {
      return var0.getNoBackupFilesDir();
   }
}
