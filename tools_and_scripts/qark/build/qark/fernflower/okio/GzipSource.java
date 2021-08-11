package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Inflater;

public final class GzipSource implements Source {
   private static final byte FCOMMENT = 4;
   private static final byte FEXTRA = 2;
   private static final byte FHCRC = 1;
   private static final byte FNAME = 3;
   private static final byte SECTION_BODY = 1;
   private static final byte SECTION_DONE = 3;
   private static final byte SECTION_HEADER = 0;
   private static final byte SECTION_TRAILER = 2;
   private final CRC32 crc = new CRC32();
   private final Inflater inflater;
   private final InflaterSource inflaterSource;
   private int section = 0;
   private final BufferedSource source;

   public GzipSource(Source var1) {
      if (var1 != null) {
         this.inflater = new Inflater(true);
         BufferedSource var2 = Okio.buffer(var1);
         this.source = var2;
         this.inflaterSource = new InflaterSource(var2, this.inflater);
      } else {
         throw new IllegalArgumentException("source == null");
      }
   }

   private void checkEqual(String var1, int var2, int var3) throws IOException {
      if (var3 != var2) {
         throw new IOException(String.format("%s: actual 0x%08x != expected 0x%08x", var1, var3, var2));
      }
   }

   private void consumeHeader() throws IOException {
      this.source.require(10L);
      byte var2 = this.source.buffer().getByte(3L);
      boolean var1;
      if ((var2 >> 1 & 1) == 1) {
         var1 = true;
      } else {
         var1 = false;
      }

      if (var1) {
         this.updateCrc(this.source.buffer(), 0L, 10L);
      }

      this.checkEqual("ID1ID2", 8075, this.source.readShort());
      this.source.skip(8L);
      if ((var2 >> 2 & 1) == 1) {
         this.source.require(2L);
         if (var1) {
            this.updateCrc(this.source.buffer(), 0L, 2L);
         }

         short var3 = this.source.buffer().readShortLe();
         this.source.require((long)var3);
         if (var1) {
            this.updateCrc(this.source.buffer(), 0L, (long)var3);
         }

         this.source.skip((long)var3);
      }

      long var4;
      if ((var2 >> 3 & 1) == 1) {
         var4 = this.source.indexOf((byte)0);
         if (var4 == -1L) {
            throw new EOFException();
         }

         if (var1) {
            this.updateCrc(this.source.buffer(), 0L, var4 + 1L);
         }

         this.source.skip(var4 + 1L);
      }

      if ((var2 >> 4 & 1) == 1) {
         var4 = this.source.indexOf((byte)0);
         if (var4 == -1L) {
            throw new EOFException();
         }

         if (var1) {
            this.updateCrc(this.source.buffer(), 0L, var4 + 1L);
         }

         this.source.skip(1L + var4);
      }

      if (var1) {
         this.checkEqual("FHCRC", this.source.readShortLe(), (short)((int)this.crc.getValue()));
         this.crc.reset();
      }

   }

   private void consumeTrailer() throws IOException {
      this.checkEqual("CRC", this.source.readIntLe(), (int)this.crc.getValue());
      this.checkEqual("ISIZE", this.source.readIntLe(), (int)this.inflater.getBytesWritten());
   }

   private void updateCrc(Buffer var1, long var2, long var4) {
      Segment var13 = var1.head;

      while(true) {
         Segment var12 = var13;
         long var8 = var2;
         long var10 = var4;
         if (var2 < (long)(var13.limit - var13.pos)) {
            while(var10 > 0L) {
               int var6 = (int)((long)var12.pos + var8);
               int var7 = (int)Math.min((long)(var12.limit - var6), var10);
               this.crc.update(var12.data, var6, var7);
               var10 -= (long)var7;
               var8 = 0L;
               var12 = var12.next;
            }

            return;
         }

         var2 -= (long)(var13.limit - var13.pos);
         var13 = var13.next;
      }
   }

   public void close() throws IOException {
      this.inflaterSource.close();
   }

   public long read(Buffer var1, long var2) throws IOException {
      if (var2 >= 0L) {
         if (var2 == 0L) {
            return 0L;
         } else {
            if (this.section == 0) {
               this.consumeHeader();
               this.section = 1;
            }

            if (this.section == 1) {
               long var4 = var1.size;
               var2 = this.inflaterSource.read(var1, var2);
               if (var2 != -1L) {
                  this.updateCrc(var1, var4, var2);
                  return var2;
               }

               this.section = 2;
            }

            if (this.section == 2) {
               this.consumeTrailer();
               this.section = 3;
               if (this.source.exhausted()) {
                  return -1L;
               } else {
                  throw new IOException("gzip finished without exhausting source");
               }
            } else {
               return -1L;
            }
         }
      } else {
         StringBuilder var6 = new StringBuilder();
         var6.append("byteCount < 0: ");
         var6.append(var2);
         throw new IllegalArgumentException(var6.toString());
      }
   }

   public Timeout timeout() {
      return this.source.timeout();
   }
}
