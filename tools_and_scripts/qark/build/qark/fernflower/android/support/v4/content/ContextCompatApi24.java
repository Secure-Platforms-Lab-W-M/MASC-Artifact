package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import java.io.File;

@TargetApi(24)
@RequiresApi(24)
class ContextCompatApi24 {
   public static Context createDeviceProtectedStorageContext(Context var0) {
      return var0.createDeviceProtectedStorageContext();
   }

   public static File getDataDir(Context var0) {
      return var0.getDataDir();
   }

   public static boolean isDeviceProtectedStorage(Context var0) {
      return var0.isDeviceProtectedStorage();
   }
}
