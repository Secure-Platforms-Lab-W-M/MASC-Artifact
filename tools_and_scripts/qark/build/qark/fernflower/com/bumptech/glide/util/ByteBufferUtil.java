package com.bumptech.glide.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.concurrent.atomic.AtomicReference;

public final class ByteBufferUtil {
   private static final AtomicReference BUFFER_REF = new AtomicReference();
   private static final int BUFFER_SIZE = 16384;

   private ByteBufferUtil() {
   }

   public static ByteBuffer fromFile(File var0) throws IOException {
      MappedByteBuffer var6 = null;
      FileChannel var5 = null;
      RandomAccessFile var3 = var6;
      FileChannel var4 = var5;

      Throwable var10000;
      label522: {
         long var1;
         boolean var10001;
         try {
            var1 = var0.length();
         } catch (Throwable var76) {
            var10000 = var76;
            var10001 = false;
            break label522;
         }

         if (var1 <= 2147483647L) {
            if (var1 != 0L) {
               label526: {
                  var3 = var6;
                  var4 = var5;

                  RandomAccessFile var77;
                  try {
                     var77 = new RandomAccessFile(var0, "r");
                  } catch (Throwable var73) {
                     var10000 = var73;
                     var10001 = false;
                     break label526;
                  }

                  var3 = var77;
                  var4 = var5;

                  try {
                     var5 = var77.getChannel();
                  } catch (Throwable var72) {
                     var10000 = var72;
                     var10001 = false;
                     break label526;
                  }

                  var3 = var77;
                  var4 = var5;

                  try {
                     var6 = var5.map(MapMode.READ_ONLY, 0L, var1).load();
                  } catch (Throwable var71) {
                     var10000 = var71;
                     var10001 = false;
                     break label526;
                  }

                  if (var5 != null) {
                     try {
                        var5.close();
                     } catch (IOException var70) {
                     }
                  }

                  try {
                     var77.close();
                     return var6;
                  } catch (IOException var69) {
                     return var6;
                  }
               }
            } else {
               var3 = var6;
               var4 = var5;

               label513:
               try {
                  throw new IOException("File unsuitable for memory mapping");
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label513;
               }
            }
         } else {
            var3 = var6;
            var4 = var5;

            label517:
            try {
               throw new IOException("File too large to map into memory");
            } catch (Throwable var75) {
               var10000 = var75;
               var10001 = false;
               break label517;
            }
         }
      }

      Throwable var78 = var10000;
      if (var4 != null) {
         try {
            var4.close();
         } catch (IOException var68) {
         }
      }

      if (var3 != null) {
         try {
            var3.close();
         } catch (IOException var67) {
         }
      }

      throw var78;
   }

   public static ByteBuffer fromStream(InputStream var0) throws IOException {
      ByteArrayOutputStream var4 = new ByteArrayOutputStream(16384);
      byte[] var3 = (byte[])BUFFER_REF.getAndSet((Object)null);
      byte[] var2 = var3;
      if (var3 == null) {
         var2 = new byte[16384];
      }

      while(true) {
         int var1 = var0.read(var2);
         if (var1 < 0) {
            BUFFER_REF.set(var2);
            byte[] var5 = var4.toByteArray();
            return (ByteBuffer)ByteBuffer.allocateDirect(var5.length).put(var5).position(0);
         }

         var4.write(var2, 0, var1);
      }
   }

   private static ByteBufferUtil.SafeArray getSafeArray(ByteBuffer var0) {
      return !var0.isReadOnly() && var0.hasArray() ? new ByteBufferUtil.SafeArray(var0.array(), var0.arrayOffset(), var0.limit()) : null;
   }

   public static byte[] toBytes(ByteBuffer var0) {
      ByteBufferUtil.SafeArray var1 = getSafeArray(var0);
      if (var1 != null && var1.offset == 0 && var1.limit == var1.data.length) {
         return var0.array();
      } else {
         var0 = var0.asReadOnlyBuffer();
         byte[] var2 = new byte[var0.limit()];
         var0.position(0);
         var0.get(var2);
         return var2;
      }
   }

   public static void toFile(ByteBuffer var0, File var1) throws IOException {
      var0.position(0);
      RandomAccessFile var2 = null;
      FileChannel var4 = null;
      FileChannel var3 = var4;

      RandomAccessFile var76;
      label518: {
         Throwable var10000;
         label519: {
            boolean var10001;
            try {
               var76 = new RandomAccessFile(var1, "rw");
            } catch (Throwable var74) {
               var10000 = var74;
               var10001 = false;
               break label519;
            }

            var2 = var76;
            var3 = var4;

            try {
               var4 = var76.getChannel();
            } catch (Throwable var73) {
               var10000 = var73;
               var10001 = false;
               break label519;
            }

            var2 = var76;
            var3 = var4;

            try {
               var4.write(var0);
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label519;
            }

            var2 = var76;
            var3 = var4;

            try {
               var4.force(false);
            } catch (Throwable var71) {
               var10000 = var71;
               var10001 = false;
               break label519;
            }

            var2 = var76;
            var3 = var4;

            try {
               var4.close();
            } catch (Throwable var70) {
               var10000 = var70;
               var10001 = false;
               break label519;
            }

            var2 = var76;
            var3 = var4;

            label493:
            try {
               var76.close();
               break label518;
            } catch (Throwable var69) {
               var10000 = var69;
               var10001 = false;
               break label493;
            }
         }

         Throwable var75 = var10000;
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var66) {
            }
         }

         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var65) {
            }
         }

         throw var75;
      }

      if (var4 != null) {
         try {
            var4.close();
         } catch (IOException var68) {
         }
      }

      try {
         var76.close();
      } catch (IOException var67) {
      }
   }

   public static InputStream toStream(ByteBuffer var0) {
      return new ByteBufferUtil.ByteBufferStream(var0);
   }

   public static void toStream(ByteBuffer var0, OutputStream var1) throws IOException {
      ByteBufferUtil.SafeArray var3 = getSafeArray(var0);
      if (var3 != null) {
         var1.write(var3.data, var3.offset, var3.offset + var3.limit);
      } else {
         byte[] var4 = (byte[])BUFFER_REF.getAndSet((Object)null);
         byte[] var5 = var4;
         if (var4 == null) {
            var5 = new byte[16384];
         }

         while(var0.remaining() > 0) {
            int var2 = Math.min(var0.remaining(), var5.length);
            var0.get(var5, 0, var2);
            var1.write(var5, 0, var2);
         }

         BUFFER_REF.set(var5);
      }
   }

   private static class ByteBufferStream extends InputStream {
      private static final int UNSET = -1;
      private final ByteBuffer byteBuffer;
      private int markPos = -1;

      ByteBufferStream(ByteBuffer var1) {
         this.byteBuffer = var1;
      }

      public int available() {
         return this.byteBuffer.remaining();
      }

      public void mark(int var1) {
         synchronized(this){}

         try {
            this.markPos = this.byteBuffer.position();
         } finally {
            ;
         }

      }

      public boolean markSupported() {
         return true;
      }

      public int read() {
         return !this.byteBuffer.hasRemaining() ? -1 : this.byteBuffer.get() & 255;
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         if (!this.byteBuffer.hasRemaining()) {
            return -1;
         } else {
            var3 = Math.min(var3, this.available());
            this.byteBuffer.get(var1, var2, var3);
            return var3;
         }
      }

      public void reset() throws IOException {
         synchronized(this){}

         try {
            if (this.markPos == -1) {
               throw new IOException("Cannot reset to unset mark position");
            }

            this.byteBuffer.position(this.markPos);
         } finally {
            ;
         }

      }

      public long skip(long var1) throws IOException {
         if (!this.byteBuffer.hasRemaining()) {
            return -1L;
         } else {
            var1 = Math.min(var1, (long)this.available());
            ByteBuffer var3 = this.byteBuffer;
            var3.position((int)((long)var3.position() + var1));
            return var1;
         }
      }
   }

   static final class SafeArray {
      final byte[] data;
      final int limit;
      final int offset;

      SafeArray(byte[] var1, int var2, int var3) {
         this.data = var1;
         this.offset = var2;
         this.limit = var3;
      }
   }
}
