package androidx.multidex;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.CRC32;
import java.util.zip.ZipException;

final class ZipUtil {
   private static final int BUFFER_SIZE = 16384;
   private static final int ENDHDR = 22;
   private static final int ENDSIG = 101010256;

   static long computeCrcOfCentralDir(RandomAccessFile var0, ZipUtil.CentralDirectory var1) throws IOException {
      CRC32 var5 = new CRC32();
      long var3 = var1.size;
      var0.seek(var1.offset);
      int var2 = (int)Math.min(16384L, var3);
      byte[] var6 = new byte[16384];

      for(var2 = var0.read(var6, 0, var2); var2 != -1; var2 = var0.read(var6, 0, (int)Math.min(16384L, var3))) {
         var5.update(var6, 0, var2);
         var3 -= (long)var2;
         if (var3 == 0L) {
            break;
         }
      }

      return var5.getValue();
   }

   static ZipUtil.CentralDirectory findCentralDirectory(RandomAccessFile var0) throws IOException, ZipException {
      long var4 = var0.length() - 22L;
      if (var4 < 0L) {
         StringBuilder var9 = new StringBuilder();
         var9.append("File too short to be a zip file: ");
         var9.append(var0.length());
         throw new ZipException(var9.toString());
      } else {
         long var6 = var4 - 65536L;
         long var2 = var6;
         if (var6 < 0L) {
            var2 = 0L;
         }

         int var1 = Integer.reverseBytes(101010256);

         do {
            var0.seek(var4);
            if (var0.readInt() == var1) {
               var0.skipBytes(2);
               var0.skipBytes(2);
               var0.skipBytes(2);
               var0.skipBytes(2);
               ZipUtil.CentralDirectory var8 = new ZipUtil.CentralDirectory();
               var8.size = (long)Integer.reverseBytes(var0.readInt()) & 4294967295L;
               var8.offset = (long)Integer.reverseBytes(var0.readInt()) & 4294967295L;
               return var8;
            }

            --var4;
         } while(var4 >= var2);

         throw new ZipException("End Of Central Directory signature not found");
      }
   }

   static long getZipCrc(File var0) throws IOException {
      RandomAccessFile var6 = new RandomAccessFile(var0, "r");

      long var1;
      try {
         var1 = computeCrcOfCentralDir(var6, findCentralDirectory(var6));
      } finally {
         var6.close();
      }

      return var1;
   }

   static class CentralDirectory {
      long offset;
      long size;
   }
}
