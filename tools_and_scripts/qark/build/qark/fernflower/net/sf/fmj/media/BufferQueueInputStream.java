package net.sf.fmj.media;

import com.lti.utils.synchronization.ProducerConsumerQueue;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.media.Buffer;
import net.sf.fmj.utility.LoggerSingleton;

public class BufferQueueInputStream extends InputStream {
   private static final int DEFAULT_QUEUE_SIZE = 20;
   private static final Logger logger;
   private int available;
   private Buffer buffer;
   // $FF: renamed from: q com.lti.utils.synchronization.ProducerConsumerQueue
   private final ProducerConsumerQueue field_174;
   private boolean trace;

   static {
      logger = LoggerSingleton.logger;
   }

   public BufferQueueInputStream() {
      this(20);
   }

   public BufferQueueInputStream(int var1) {
      this.trace = false;
      this.available = 0;
      this.field_174 = new ProducerConsumerQueue(var1);
   }

   public BufferQueueInputStream(ProducerConsumerQueue var1) {
      this.trace = false;
      this.available = 0;
      this.field_174 = var1;
   }

   private void fillBuffer() throws IOException {
      // $FF: Couldn't be decompiled
   }

   private boolean put(Buffer param1, boolean param2, boolean param3) {
      // $FF: Couldn't be decompiled
   }

   public int available() {
      ProducerConsumerQueue var2 = this.field_174;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.trace) {
               Logger var3 = logger;
               StringBuilder var4 = new StringBuilder();
               var4.append(this);
               var4.append(" available: available=");
               var4.append(this.available);
               var3.fine(var4.toString());
            }
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            int var1 = this.available;
            return var1;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var17 = var10000;

         try {
            throw var17;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            continue;
         }
      }
   }

   public void blockingPut(Buffer var1) {
      this.blockingPut(var1, true);
   }

   public void blockingPut(Buffer var1, boolean var2) {
      this.put(var1, true, var2);
   }

   public boolean put(Buffer var1) {
      return this.put(var1, true);
   }

   public boolean put(Buffer var1, boolean var2) {
      return this.put(var1, false, var2);
   }

   public int read() throws IOException {
      this.fillBuffer();
      Logger var18;
      StringBuilder var20;
      if (this.buffer.getLength() <= 0 && this.buffer.isEOM()) {
         if (this.trace) {
            var18 = logger;
            var20 = new StringBuilder();
            var20.append(this);
            var20.append(" BufferQueueInputStream.read: returning -1");
            var18.fine(var20.toString());
         }

         return -1;
      } else {
         int var1 = ((byte[])((byte[])this.buffer.getData()))[this.buffer.getOffset()] & 255;
         Buffer var2 = this.buffer;
         var2.setOffset(var2.getOffset() + 1);
         var2 = this.buffer;
         var2.setLength(var2.getLength() - 1);
         ProducerConsumerQueue var17 = this.field_174;
         synchronized(var17){}

         label192: {
            Throwable var10000;
            boolean var10001;
            label185: {
               try {
                  --this.available;
                  if (this.trace) {
                     Logger var3 = logger;
                     StringBuilder var4 = new StringBuilder();
                     var4.append(this);
                     var4.append(" read: available=");
                     var4.append(this.available);
                     var3.fine(var4.toString());
                  }
               } catch (Throwable var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label185;
               }

               label182:
               try {
                  break label192;
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label182;
               }
            }

            while(true) {
               Throwable var19 = var10000;

               try {
                  throw var19;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  continue;
               }
            }
         }

         if (this.trace) {
            var18 = logger;
            var20 = new StringBuilder();
            var20.append(this);
            var20.append(" BufferQueueInputStream.read: returning ");
            var20.append(var1);
            var18.fine(var20.toString());
         }

         return var1;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      this.fillBuffer();
      Logger var23;
      StringBuilder var26;
      if (this.buffer.getLength() <= 0 && this.buffer.isEOM()) {
         if (this.trace) {
            var23 = logger;
            var26 = new StringBuilder();
            var26.append(this);
            var26.append(" BufferQueueInputStream.read: returning -1");
            var23.fine(var26.toString());
         }

         return -1;
      } else {
         byte[] var5 = (byte[])((byte[])this.buffer.getData());
         if (var5 != null) {
            int var4;
            if (this.buffer.getLength() < var3) {
               var4 = this.buffer.getLength();
            } else {
               var4 = var3;
            }

            if (this.trace) {
               Logger var6 = logger;
               StringBuilder var7 = new StringBuilder();
               var7.append(this);
               var7.append(" BufferQueueInputStream.read: lengthToCopy=");
               var7.append(var4);
               var7.append(" buffer.getLength()=");
               var7.append(this.buffer.getLength());
               var7.append(" buffer.getOffset()=");
               var7.append(this.buffer.getOffset());
               var7.append(" b.length=");
               var7.append(var1.length);
               var7.append(" len=");
               var7.append(var3);
               var7.append(" off=");
               var7.append(var2);
               var6.fine(var7.toString());
            }

            System.arraycopy(var5, this.buffer.getOffset(), var1, var2, var4);
            Buffer var21 = this.buffer;
            var21.setOffset(var21.getOffset() + var4);
            var21 = this.buffer;
            var21.setLength(var21.getLength() - var4);
            ProducerConsumerQueue var22 = this.field_174;
            synchronized(var22){}

            label248: {
               Throwable var10000;
               boolean var10001;
               label236: {
                  try {
                     this.available -= var4;
                     if (this.trace) {
                        Logger var24 = logger;
                        StringBuilder var27 = new StringBuilder();
                        var27.append(this);
                        var27.append(" read: available=");
                        var27.append(this.available);
                        var24.fine(var27.toString());
                     }
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label236;
                  }

                  label233:
                  try {
                     break label248;
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label233;
                  }
               }

               while(true) {
                  Throwable var25 = var10000;

                  try {
                     throw var25;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     continue;
                  }
               }
            }

            if (this.trace) {
               var23 = logger;
               var26 = new StringBuilder();
               var26.append(this);
               var26.append(" BufferQueueInputStream.read[]: returning ");
               var26.append(var4);
               var23.fine(var26.toString());
            }

            return var4;
         } else {
            StringBuilder var20 = new StringBuilder();
            var20.append("Buffer has null data.  length=");
            var20.append(this.buffer.getLength());
            var20.append(" offset=");
            var20.append(this.buffer.getOffset());
            throw new NullPointerException(var20.toString());
         }
      }
   }

   public void setTrace(boolean var1) {
      this.trace = var1;
   }
}
