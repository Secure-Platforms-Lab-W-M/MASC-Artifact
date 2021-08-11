package okio;

import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

public final class GzipSink implements Sink {
   private boolean closed;
   private final CRC32 crc = new CRC32();
   private final Deflater deflater;
   private final DeflaterSink deflaterSink;
   private final BufferedSink sink;

   public GzipSink(Sink var1) {
      if (var1 != null) {
         this.deflater = new Deflater(-1, true);
         BufferedSink var2 = Okio.buffer(var1);
         this.sink = var2;
         this.deflaterSink = new DeflaterSink(var2, this.deflater);
         this.writeHeader();
      } else {
         throw new IllegalArgumentException("sink == null");
      }
   }

   private void updateCrc(Buffer var1, long var2) {
      for(Segment var5 = var1.head; var2 > 0L; var5 = var5.next) {
         int var4 = (int)Math.min(var2, (long)(var5.limit - var5.pos));
         this.crc.update(var5.data, var5.pos, var4);
         var2 -= (long)var4;
      }

   }

   private void writeFooter() throws IOException {
      this.sink.writeIntLe((int)this.crc.getValue());
      this.sink.writeIntLe((int)this.deflater.getBytesRead());
   }

   private void writeHeader() {
      Buffer var1 = this.sink.buffer();
      var1.writeShort(8075);
      var1.writeByte(8);
      var1.writeByte(0);
      var1.writeInt(0);
      var1.writeByte(0);
      var1.writeByte(0);
   }

   public void close() throws IOException {
      if (!this.closed) {
         Throwable var2 = null;

         label168:
         try {
            this.deflaterSink.finishDeflate();
            this.writeFooter();
         } finally {
            break label168;
         }

         Throwable var1;
         label165: {
            try {
               this.deflater.end();
            } catch (Throwable var14) {
               var1 = var2;
               if (var2 == null) {
                  var1 = var14;
               }
               break label165;
            }

            var1 = var2;
         }

         label158: {
            try {
               this.sink.close();
            } catch (Throwable var13) {
               var2 = var1;
               if (var1 == null) {
                  var2 = var13;
               }
               break label158;
            }

            var2 = var1;
         }

         this.closed = true;
         if (var2 != null) {
            Util.sneakyRethrow(var2);
         }

      }
   }

   public Deflater deflater() {
      return this.deflater;
   }

   public void flush() throws IOException {
      this.deflaterSink.flush();
   }

   public Timeout timeout() {
      return this.sink.timeout();
   }

   public void write(Buffer var1, long var2) throws IOException {
      if (var2 >= 0L) {
         if (var2 != 0L) {
            this.updateCrc(var1, var2);
            this.deflaterSink.write(var1, var2);
         }
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("byteCount < 0: ");
         var4.append(var2);
         throw new IllegalArgumentException(var4.toString());
      }
   }
}
