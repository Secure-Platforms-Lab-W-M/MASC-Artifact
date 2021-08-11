package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import java.io.FileDescriptor;
import java.io.PrintWriter;

@TargetApi(11)
@RequiresApi(11)
class ActivityCompatHoneycomb {
   static void dump(Activity var0, String var1, FileDescriptor var2, PrintWriter var3, String[] var4) {
      var0.dump(var1, var2, var3, var4);
   }

   static void invalidateOptionsMenu(Activity var0) {
      var0.invalidateOptionsMenu();
   }
}
