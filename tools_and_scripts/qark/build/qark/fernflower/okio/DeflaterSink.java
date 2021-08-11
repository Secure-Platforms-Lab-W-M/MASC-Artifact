package okio;

import java.io.IOException;
import java.util.zip.Deflater;

public final class DeflaterSink implements Sink {
   private boolean closed;
   private final Deflater deflater;
   private final BufferedSink sink;

   DeflaterSink(BufferedSink var1, Deflater var2) {
      if (var1 != null) {
         if (var2 != null) {
            this.sink = var1;
            this.deflater = var2;
         } else {
            throw new IllegalArgumentException("inflater == null");
         }
      } else {
         throw new IllegalArgumentException("source == null");
      }
   }

   public DeflaterSink(Sink var1, Deflater var2) {
      this(Okio.buffer(var1), var2);
   }

   private void deflate(boolean var1) throws IOException {
      Buffer var3 = this.sink.buffer();

      while(true) {
         Segment var4 = var3.writableSegment(1);
         int var2;
         if (var1) {
            var2 = this.deflater.deflate(var4.data, var4.limit, 8192 - var4.limit, 2);
         } else {
            var2 = this.deflater.deflate(var4.data, var4.limit, 8192 - var4.limit);
         }

         if (var2 > 0) {
            var4.limit += var2;
            var3.size += (long)var2;
            this.sink.emitCompleteSegments();
         } else if (this.deflater.needsInput()) {
            if (var4.pos == var4.limit) {
               var3.head = var4.pop();
               SegmentPool.recycle(var4);
            }

            return;
         }
      }
   }

   public void close() throws IOException {
      if (!this.closed) {
         Throwable var2 = null;

         label168:
         try {
            this.finishDeflate();
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

   void finishDeflate() throws IOException {
      this.deflater.finish();
      this.deflate(false);
   }

   public void flush() throws IOException {
      this.deflate(true);
      this.sink.flush();
   }

   public Timeout timeout() {
      return this.sink.timeout();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DeflaterSink(");
      var1.append(this.sink);
      var1.append(")");
      return var1.toString();
   }

   public void write(Buffer var1, long var2) throws IOException {
      Util.checkOffsetAndCount(var1.size, 0L, var2);

      int var4;
      for(; var2 > 0L; var2 -= (long)var4) {
         Segment var5 = var1.head;
         var4 = (int)Math.min(var2, (long)(var5.limit - var5.pos));
         this.deflater.setInput(var5.data, var5.pos, var4);
         this.deflate(false);
         var1.size -= (long)var4;
         var5.pos += var4;
         if (var5.pos == var5.limit) {
            var1.head = var5.pop();
            SegmentPool.recycle(var5);
         }
      }

   }
}
