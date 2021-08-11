package androidx.core.content;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.OperationCanceledException;
import android.os.Build.VERSION;
import androidx.core.os.CancellationSignal;

public final class ContentResolverCompat {
   private ContentResolverCompat() {
   }

   public static Cursor query(ContentResolver var0, Uri var1, String[] var2, String var3, String[] var4, String var5, CancellationSignal var6) {
      if (VERSION.SDK_INT < 16) {
         if (var6 != null) {
            var6.throwIfCanceled();
         }

         return var0.query(var1, var2, var3, var4, var5);
      } else {
         Exception var10000;
         label35: {
            Object var11;
            boolean var10001;
            if (var6 != null) {
               try {
                  var11 = var6.getCancellationSignalObject();
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label35;
               }
            } else {
               var11 = null;
            }

            try {
               Cursor var10 = var0.query(var1, var2, var3, var4, var5, (android.os.CancellationSignal)((android.os.CancellationSignal)var11));
               return var10;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
            }
         }

         Exception var9 = var10000;
         if (var9 instanceof OperationCanceledException) {
            throw new androidx.core.os.OperationCanceledException();
         } else {
            throw var9;
         }
      }
   }
}
