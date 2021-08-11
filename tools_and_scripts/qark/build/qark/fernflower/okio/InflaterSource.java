package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public final class InflaterSource implements Source {
   private int bufferBytesHeldByInflater;
   private boolean closed;
   private final Inflater inflater;
   private final BufferedSource source;

   InflaterSource(BufferedSource var1, Inflater var2) {
      if (var1 != null) {
         if (var2 != null) {
            this.source = var1;
            this.inflater = var2;
         } else {
            throw new IllegalArgumentException("inflater == null");
         }
      } else {
         throw new IllegalArgumentException("source == null");
      }
   }

   public InflaterSource(Source var1, Inflater var2) {
      this(Okio.buffer(var1), var2);
   }

   private void releaseInflatedBytes() throws IOException {
      int var1 = this.bufferBytesHeldByInflater;
      if (var1 != 0) {
         var1 -= this.inflater.getRemaining();
         this.bufferBytesHeldByInflater -= var1;
         this.source.skip((long)var1);
      }
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.inflater.end();
         this.closed = true;
         this.source.close();
      }
   }

   public long read(Buffer var1, long var2) throws IOException {
      if (var2 < 0L) {
         StringBuilder var13 = new StringBuilder();
         var13.append("byteCount < 0: ");
         var13.append(var2);
         throw new IllegalArgumentException(var13.toString());
      } else if (this.closed) {
         throw new IllegalStateException("closed");
      } else if (var2 == 0L) {
         return 0L;
      } else {
         while(true) {
            boolean var5 = this.refill();

            DataFormatException var10000;
            label67: {
               int var4;
               Segment var6;
               boolean var10001;
               try {
                  var6 = var1.writableSegment(1);
                  var4 = this.inflater.inflate(var6.data, var6.limit, 8192 - var6.limit);
               } catch (DataFormatException var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label67;
               }

               if (var4 > 0) {
                  label48: {
                     try {
                        var6.limit += var4;
                        var1.size += (long)var4;
                     } catch (DataFormatException var7) {
                        var10000 = var7;
                        var10001 = false;
                        break label48;
                     }

                     return (long)var4;
                  }
               } else {
                  label74: {
                     label75: {
                        try {
                           if (!this.inflater.finished() && !this.inflater.needsDictionary()) {
                              break label75;
                           }
                        } catch (DataFormatException var10) {
                           var10000 = var10;
                           var10001 = false;
                           break label74;
                        }

                        try {
                           this.releaseInflatedBytes();
                           if (var6.pos == var6.limit) {
                              var1.head = var6.pop();
                              SegmentPool.recycle(var6);
                           }

                           return -1L;
                        } catch (DataFormatException var9) {
                           var10000 = var9;
                           var10001 = false;
                           break label74;
                        }
                     }

                     if (!var5) {
                        continue;
                     }

                     try {
                        throw new EOFException("source exhausted prematurely");
                     } catch (DataFormatException var8) {
                        var10000 = var8;
                        var10001 = false;
                     }
                  }
               }
            }

            DataFormatException var12 = var10000;
            throw new IOException(var12);
         }
      }
   }

   public boolean refill() throws IOException {
      if (!this.inflater.needsInput()) {
         return false;
      } else {
         this.releaseInflatedBytes();
         if (this.inflater.getRemaining() == 0) {
            if (this.source.exhausted()) {
               return true;
            } else {
               Segment var1 = this.source.buffer().head;
               this.bufferBytesHeldByInflater = var1.limit - var1.pos;
               this.inflater.setInput(var1.data, var1.pos, this.bufferBytesHeldByInflater);
               return false;
            }
         } else {
            throw new IllegalStateException("?");
         }
      }
   }

   public Timeout timeout() {
      return this.source.timeout();
   }
}
