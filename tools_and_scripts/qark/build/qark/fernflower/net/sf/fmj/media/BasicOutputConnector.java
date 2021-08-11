package net.sf.fmj.media;

import javax.media.Buffer;
import javax.media.Format;

public class BasicOutputConnector extends BasicConnector implements OutputConnector {
   protected InputConnector inputConnector = null;
   private boolean reset = false;

   public Format canConnectTo(InputConnector var1, Format var2) {
      if (this.getProtocol() == var1.getProtocol()) {
         return null;
      } else {
         throw new RuntimeException("protocols do not match:: ");
      }
   }

   public Format connectTo(InputConnector var1, Format var2) {
      this.canConnectTo(var1, var2);
      this.inputConnector = var1;
      var1.setOutputConnector(this);
      this.circularBuffer = new CircularBuffer(Math.max(this.getSize(), var1.getSize()));
      var1.setCircularBuffer(this.circularBuffer);
      return null;
   }

   public Buffer getEmptyBuffer() {
      // $FF: Couldn't be decompiled
   }

   public InputConnector getInputConnector() {
      return this.inputConnector;
   }

   public boolean isEmptyBufferAvailable() {
      return this.circularBuffer.canWrite();
   }

   public void reset() {
      CircularBuffer var1 = this.circularBuffer;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            this.reset = true;
            super.reset();
            if (this.inputConnector != null) {
               this.inputConnector.reset();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            this.circularBuffer.notifyAll();
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void writeReport() {
      int var1 = this.protocol;
      Throwable var10000;
      boolean var10001;
      CircularBuffer var2;
      Throwable var3;
      if (var1 != 0) {
         if (var1 == 1) {
            var2 = this.circularBuffer;
            synchronized(var2){}

            label458: {
               try {
                  if (this.reset) {
                     return;
                  }
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label458;
               }

               label452:
               try {
                  this.circularBuffer.writeReport();
                  this.circularBuffer.notifyAll();
                  return;
               } catch (Throwable var42) {
                  var10000 = var42;
                  var10001 = false;
                  break label452;
               }
            }

            while(true) {
               var3 = var10000;

               try {
                  throw var3;
               } catch (Throwable var41) {
                  var10000 = var41;
                  var10001 = false;
                  continue;
               }
            }
         } else {
            throw new RuntimeException();
         }
      } else {
         var2 = this.circularBuffer;
         synchronized(var2){}

         label479: {
            try {
               if (this.reset) {
                  return;
               }
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label479;
            }

            try {
               this.circularBuffer.writeReport();
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label479;
            }

            this.getInputConnector().getModule().connectorPushed(this.getInputConnector());
            return;
         }

         while(true) {
            var3 = var10000;

            try {
               throw var3;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               continue;
            }
         }
      }
   }
}
