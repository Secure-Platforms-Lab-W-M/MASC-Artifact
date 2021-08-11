package okhttp3.internal.cache2;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

final class Relay {
   private static final long FILE_HEADER_SIZE = 32L;
   static final ByteString PREFIX_CLEAN = ByteString.encodeUtf8("OkHttp cache v1\n");
   static final ByteString PREFIX_DIRTY = ByteString.encodeUtf8("OkHttp DIRTY :(\n");
   private static final int SOURCE_FILE = 2;
   private static final int SOURCE_UPSTREAM = 1;
   final Buffer buffer = new Buffer();
   final long bufferMaxSize;
   boolean complete;
   RandomAccessFile file;
   private final ByteString metadata;
   int sourceCount;
   Source upstream;
   final Buffer upstreamBuffer = new Buffer();
   long upstreamPos;
   Thread upstreamReader;

   private Relay(RandomAccessFile var1, Source var2, long var3, ByteString var5, long var6) {
      this.file = var1;
      this.upstream = var2;
      boolean var8;
      if (var2 == null) {
         var8 = true;
      } else {
         var8 = false;
      }

      this.complete = var8;
      this.upstreamPos = var3;
      this.metadata = var5;
      this.bufferMaxSize = var6;
   }

   public static Relay edit(File var0, Source var1, ByteString var2, long var3) throws IOException {
      RandomAccessFile var5 = new RandomAccessFile(var0, "rw");
      Relay var6 = new Relay(var5, var1, 0L, var2, var3);
      var5.setLength(0L);
      var6.writeHeader(PREFIX_DIRTY, -1L, -1L);
      return var6;
   }

   public static Relay read(File var0) throws IOException {
      RandomAccessFile var7 = new RandomAccessFile(var0, "rw");
      FileOperator var5 = new FileOperator(var7.getChannel());
      Buffer var6 = new Buffer();
      var5.read(0L, var6, 32L);
      if (var6.readByteString((long)PREFIX_CLEAN.size()).equals(PREFIX_CLEAN)) {
         long var1 = var6.readLong();
         long var3 = var6.readLong();
         var6 = new Buffer();
         var5.read(var1 + 32L, var6, var3);
         return new Relay(var7, (Source)null, var1, var6.readByteString(), 0L);
      } else {
         throw new IOException("unreadable cache file");
      }
   }

   private void writeHeader(ByteString var1, long var2, long var4) throws IOException {
      Buffer var6 = new Buffer();
      var6.write(var1);
      var6.writeLong(var2);
      var6.writeLong(var4);
      if (var6.size() == 32L) {
         (new FileOperator(this.file.getChannel())).write(0L, var6, 32L);
      } else {
         throw new IllegalArgumentException();
      }
   }

   private void writeMetadata(long var1) throws IOException {
      Buffer var3 = new Buffer();
      var3.write(this.metadata);
      (new FileOperator(this.file.getChannel())).write(32L + var1, var3, (long)this.metadata.size());
   }

   void commit(long param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   boolean isClosed() {
      return this.file == null;
   }

   public ByteString metadata() {
      return this.metadata;
   }

   public Source newSource() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.file == null) {
               return null;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label137;
         }

         try {
            ++this.sourceCount;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label137;
         }

         return new Relay.RelaySource();
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   class RelaySource implements Source {
      private FileOperator fileOperator;
      private long sourcePos;
      private final Timeout timeout = new Timeout();

      RelaySource() {
         this.fileOperator = new FileOperator(Relay.this.file.getChannel());
      }

      public void close() throws IOException {
         if (this.fileOperator != null) {
            this.fileOperator = null;
            RandomAccessFile var1 = null;
            Relay var2 = Relay.this;
            synchronized(var2){}

            label239: {
               Throwable var10000;
               boolean var10001;
               label240: {
                  try {
                     Relay var3 = Relay.this;
                     --var3.sourceCount;
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label240;
                  }

                  try {
                     if (Relay.this.sourceCount == 0) {
                        var1 = Relay.this.file;
                        Relay.this.file = null;
                     }
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label240;
                  }

                  label226:
                  try {
                     break label239;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label226;
                  }
               }

               while(true) {
                  Throwable var24 = var10000;

                  try {
                     throw var24;
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     continue;
                  }
               }
            }

            if (var1 != null) {
               Util.closeQuietly((Closeable)var1);
            }

         }
      }

      public long read(Buffer param1, long param2) throws IOException {
         // $FF: Couldn't be decompiled
      }

      public Timeout timeout() {
         return this.timeout;
      }
   }
}
