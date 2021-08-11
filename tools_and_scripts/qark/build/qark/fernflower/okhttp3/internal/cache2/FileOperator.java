package okhttp3.internal.cache2;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import okio.Buffer;

final class FileOperator {
   private static final int BUFFER_SIZE = 8192;
   private final byte[] byteArray;
   private final ByteBuffer byteBuffer;
   private final FileChannel fileChannel;

   FileOperator(FileChannel var1) {
      byte[] var2 = new byte[8192];
      this.byteArray = var2;
      this.byteBuffer = ByteBuffer.wrap(var2);
      this.fileChannel = var1;
   }

   public void read(long var1, Buffer var3, long var4) throws IOException {
      if (var4 < 0L) {
         throw new IndexOutOfBoundsException();
      } else {
         while(var4 > 0L) {
            boolean var8 = false;

            int var6;
            try {
               var8 = true;
               this.byteBuffer.limit((int)Math.min(8192L, var4));
               if (this.fileChannel.read(this.byteBuffer, var1) == -1) {
                  throw new EOFException();
               }

               var6 = this.byteBuffer.position();
               var3.write(this.byteArray, 0, var6);
               var8 = false;
            } finally {
               if (var8) {
                  this.byteBuffer.clear();
               }
            }

            var1 += (long)var6;
            var4 -= (long)var6;
            this.byteBuffer.clear();
         }

      }
   }

   public void write(long var1, Buffer var3, long var4) throws IOException {
      if (var4 >= 0L && var4 <= var3.size()) {
         label108:
         while(var4 > 0L) {
            Throwable var10000;
            label114: {
               boolean var10001;
               int var6;
               try {
                  var6 = (int)Math.min(8192L, var4);
                  var3.read(this.byteArray, 0, var6);
                  this.byteBuffer.limit(var6);
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label114;
               }

               long var7 = var1;

               while(true) {
                  boolean var9;
                  try {
                     var1 = var7 + (long)this.fileChannel.write(this.byteBuffer, var7);
                     var9 = this.byteBuffer.hasRemaining();
                  } catch (Throwable var14) {
                     var10000 = var14;
                     var10001 = false;
                     break;
                  }

                  var7 = var1;
                  if (!var9) {
                     var4 -= (long)var6;
                     this.byteBuffer.clear();
                     continue label108;
                  }
               }
            }

            Throwable var16 = var10000;
            this.byteBuffer.clear();
            throw var16;
         }

      } else {
         throw new IndexOutOfBoundsException();
      }
   }
}
