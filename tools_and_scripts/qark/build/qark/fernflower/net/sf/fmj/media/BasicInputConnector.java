package net.sf.fmj.media;

import javax.media.Buffer;

public class BasicInputConnector extends BasicConnector implements InputConnector {
   protected OutputConnector outputConnector = null;
   private boolean reset = false;

   public OutputConnector getOutputConnector() {
      return this.outputConnector;
   }

   public Buffer getValidBuffer() {
      // $FF: Couldn't be decompiled
   }

   public boolean isValidBufferAvailable() {
      return this.circularBuffer.canRead();
   }

   public void readReport() {
      int var1 = this.protocol;
      if (var1 != 0 && var1 != 1) {
         throw new RuntimeException();
      } else {
         CircularBuffer var2 = this.circularBuffer;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label146: {
            try {
               if (this.reset) {
                  return;
               }
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label146;
            }

            label140:
            try {
               this.circularBuffer.readReport();
               this.circularBuffer.notifyAll();
               return;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label140;
            }
         }

         while(true) {
            Throwable var3 = var10000;

            try {
               throw var3;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public void reset() {
      // $FF: Couldn't be decompiled
   }

   public void setOutputConnector(OutputConnector var1) {
      this.outputConnector = var1;
   }
}
