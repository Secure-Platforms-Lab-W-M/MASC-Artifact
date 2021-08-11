package net.sf.fmj.media;

import com.lti.utils.synchronization.CloseableThread;
import com.lti.utils.synchronization.ProducerConsumerQueue;
import com.lti.utils.synchronization.SynchronizedObjectHolder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.PushBufferStream;
import net.sf.fmj.utility.LoggerSingleton;

public class AsyncBufferTransferHandlerNotifier {
   private static final Logger logger;
   private AsyncBufferTransferHandlerNotifier.NotifyTransferHandlerThread notifyTransferHandlerThread;
   private final PushBufferStream stream;
   private final SynchronizedObjectHolder transferHandlerHolder = new SynchronizedObjectHolder();

   static {
      logger = LoggerSingleton.logger;
   }

   public AsyncBufferTransferHandlerNotifier(PushBufferStream var1) {
      this.stream = var1;
   }

   public void dispose() {
      AsyncBufferTransferHandlerNotifier.NotifyTransferHandlerThread var1 = this.notifyTransferHandlerThread;
      if (var1 != null) {
         var1.close();

         try {
            this.notifyTransferHandlerThread.waitUntilClosed();
         } catch (InterruptedException var7) {
            Logger var2 = logger;
            Level var3 = Level.WARNING;
            StringBuilder var4 = new StringBuilder();
            var4.append("");
            var4.append(var7);
            var2.log(var3, var4.toString(), var7);
         } finally {
            this.notifyTransferHandlerThread = null;
         }

      }
   }

   public void disposeAsync() {
      AsyncBufferTransferHandlerNotifier.NotifyTransferHandlerThread var1 = this.notifyTransferHandlerThread;
      if (var1 != null) {
         var1.close();
         this.notifyTransferHandlerThread = null;
      }

   }

   public void notifyTransferHandlerAsync() throws InterruptedException {
      if (this.notifyTransferHandlerThread == null) {
         StringBuilder var1 = new StringBuilder();
         var1.append("NotifyTransferHandlerThread for ");
         var1.append(this.stream);
         AsyncBufferTransferHandlerNotifier.NotifyTransferHandlerThread var2 = new AsyncBufferTransferHandlerNotifier.NotifyTransferHandlerThread(var1.toString());
         this.notifyTransferHandlerThread = var2;
         var2.start();
      }

      this.notifyTransferHandlerThread.notifyTransferHandlerAsync();
   }

   public void notifyTransferHandlerSync() {
      BufferTransferHandler var1 = (BufferTransferHandler)this.transferHandlerHolder.getObject();
      if (var1 != null) {
         var1.transferData(this.stream);
      }

   }

   public void setTransferHandler(BufferTransferHandler var1) {
      this.transferHandlerHolder.setObject(var1);
   }

   class NotifyTransferHandlerThread extends CloseableThread {
      // $FF: renamed from: q com.lti.utils.synchronization.ProducerConsumerQueue
      private final ProducerConsumerQueue field_20 = new ProducerConsumerQueue();

      public NotifyTransferHandlerThread(String var2) {
         super(var2);
         this.setDaemon(true);
      }

      public void notifyTransferHandlerAsync() throws InterruptedException {
         this.field_20.put(Boolean.TRUE);
      }

      public void run() {
         while(true) {
            boolean var4 = false;

            try {
               var4 = true;
               if (!this.isClosing()) {
                  if (this.field_20.get() != null) {
                     AsyncBufferTransferHandlerNotifier.this.notifyTransferHandlerSync();
                     var4 = false;
                     continue;
                  }

                  var4 = false;
               } else {
                  var4 = false;
               }
            } catch (InterruptedException var5) {
               var4 = false;
            } finally {
               if (var4) {
                  this.setClosed();
               }
            }

            this.setClosed();
            return;
         }
      }
   }
}
