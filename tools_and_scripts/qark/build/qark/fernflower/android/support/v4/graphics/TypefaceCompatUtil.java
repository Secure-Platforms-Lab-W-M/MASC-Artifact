package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompatUtil {
   private static final String CACHE_FILE_PREFIX = ".font";
   private static final String TAG = "TypefaceCompatUtil";

   private TypefaceCompatUtil() {
   }

   public static void closeQuietly(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
            return;
         } catch (IOException var1) {
         }
      }

   }

   @RequiresApi(19)
   public static ByteBuffer copyToDirectBuffer(Context var0, Resources var1, int var2) {
      File var4 = getTempFile(var0);
      ByteBuffer var11 = null;
      if (var4 == null) {
         return null;
      } else {
         label79: {
            Throwable var10000;
            label84: {
               boolean var10001;
               boolean var3;
               try {
                  var3 = copyToFile(var4, var1, var2);
               } catch (Throwable var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label84;
               }

               if (!var3) {
                  break label79;
               }

               label74:
               try {
                  var11 = mmap(var4);
                  break label79;
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label74;
               }
            }

            Throwable var12 = var10000;
            var4.delete();
            throw var12;
         }

         var4.delete();
         return var11;
      }
   }

   public static boolean copyToFile(File var0, Resources var1, int var2) {
      InputStream var4 = null;

      boolean var3;
      InputStream var12;
      label68: {
         Throwable var10000;
         label72: {
            boolean var10001;
            try {
               var12 = var1.openRawResource(var2);
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               break label72;
            }

            var4 = var12;

            label63:
            try {
               var3 = copyToFile(var0, var12);
               break label68;
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label63;
            }
         }

         Throwable var11 = var10000;
         closeQuietly(var4);
         throw var11;
      }

      closeQuietly(var12);
      return var3;
   }

   public static boolean copyToFile(File param0, InputStream param1) {
      // $FF: Couldn't be decompiled
   }

   public static File getTempFile(Context var0) {
      StringBuilder var3 = new StringBuilder();
      var3.append(".font");
      var3.append(Process.myPid());
      var3.append("-");
      var3.append(Process.myTid());
      var3.append("-");
      String var7 = var3.toString();

      for(int var1 = 0; var1 < 100; ++var1) {
         File var4 = var0.getCacheDir();
         StringBuilder var5 = new StringBuilder();
         var5.append(var7);
         var5.append(var1);
         var4 = new File(var4, var5.toString());

         boolean var2;
         try {
            var2 = var4.createNewFile();
         } catch (IOException var6) {
            continue;
         }

         if (var2) {
            return var4;
         }
      }

      return null;
   }

   @RequiresApi(19)
   public static ByteBuffer mmap(Context param0, CancellationSignal param1, Uri param2) {
      // $FF: Couldn't be decompiled
   }

   @RequiresApi(19)
   private static ByteBuffer mmap(File param0) {
      // $FF: Couldn't be decompiled
   }
}
