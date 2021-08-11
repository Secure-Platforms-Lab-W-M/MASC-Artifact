package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;
import android.support.annotation.RequiresApi;

@TargetApi(16)
@RequiresApi(16)
class ContentResolverCompatJellybean {
   static boolean isFrameworkOperationCanceledException(Exception var0) {
      return var0 instanceof OperationCanceledException;
   }

   public static Cursor query(ContentResolver var0, Uri var1, String[] var2, String var3, String[] var4, String var5, Object var6) {
      return var0.query(var1, var2, var3, var4, var5, (CancellationSignal)var6);
   }
}
