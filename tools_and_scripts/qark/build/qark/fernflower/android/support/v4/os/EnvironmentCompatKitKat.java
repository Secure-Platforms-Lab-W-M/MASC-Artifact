package android.support.v4.os;

import android.annotation.TargetApi;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import java.io.File;

@TargetApi(19)
@RequiresApi(19)
class EnvironmentCompatKitKat {
   public static String getStorageState(File var0) {
      return Environment.getStorageState(var0);
   }
}
