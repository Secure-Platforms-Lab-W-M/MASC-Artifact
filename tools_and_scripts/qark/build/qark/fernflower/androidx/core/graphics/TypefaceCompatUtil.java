package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Process;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

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

   public static ByteBuffer copyToDirectBuffer(Context var0, Resources var1, int var2) {
      File var10 = getTempFile(var0);
      if (var10 == null) {
         return null;
      } else {
         Throwable var10000;
         label84: {
            boolean var10001;
            boolean var3;
            try {
               var3 = copyToFile(var10, var1, var2);
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label84;
            }

            if (!var3) {
               var10.delete();
               return null;
            }

            ByteBuffer var12;
            try {
               var12 = mmap(var10);
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label84;
            }

            var10.delete();
            return var12;
         }

         Throwable var11 = var10000;
         var10.delete();
         throw var11;
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
      File var6 = var0.getCacheDir();
      if (var6 == null) {
         return null;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(".font");
         var3.append(Process.myPid());
         var3.append("-");
         var3.append(Process.myTid());
         var3.append("-");
         String var7 = var3.toString();

         for(int var1 = 0; var1 < 100; ++var1) {
            StringBuilder var4 = new StringBuilder();
            var4.append(var7);
            var4.append(var1);
            File var8 = new File(var6, var4.toString());

            boolean var2;
            try {
               var2 = var8.createNewFile();
            } catch (IOException var5) {
               continue;
            }

            if (var2) {
               return var8;
            }
         }

         return null;
      }
   }

   public static ByteBuffer mmap(Context param0, CancellationSignal param1, Uri param2) {
      // $FF: Couldn't be decompiled
   }

   private static ByteBuffer mmap(File var0) {
      FileInputStream var33;
      boolean var10001;
      try {
         var33 = new FileInputStream(var0);
      } catch (IOException var32) {
         var10001 = false;
         return null;
      }

      MappedByteBuffer var36;
      try {
         FileChannel var35 = var33.getChannel();
         long var1 = var35.size();
         var36 = var35.map(MapMode.READ_ONLY, 0L, var1);
      } catch (Throwable var31) {
         Throwable var3 = var31;

         try {
            throw var3;
         } finally {
            try {
               var33.close();
            } catch (Throwable var28) {
               Throwable var34 = var28;

               label155:
               try {
                  var3.addSuppressed(var34);
                  break label155;
               } catch (IOException var27) {
                  var10001 = false;
                  return null;
               }
            }

            try {
               ;
            } catch (IOException var26) {
               var10001 = false;
               return null;
            }
         }
      }

      try {
         var33.close();
         return var36;
      } catch (IOException var30) {
         var10001 = false;
         return null;
      }
   }
}
