package net.sf.fmj.media.codec;

import com.lti.utils.synchronization.CloseableThread;
import com.lti.utils.synchronization.ProducerConsumerQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import javax.media.Buffer;

public class InputStreamReader extends InputStream {
   private final ProducerConsumerQueue emptyQueue = new ProducerConsumerQueue();
   private final ProducerConsumerQueue fullQueue = new ProducerConsumerQueue();
   private Buffer readBuffer;
   private IOException readException;
   private InputStreamReader.ReaderThread readerThread;
   private boolean readerThreadStarted;

   public InputStreamReader(InputStream var1, int var2) {
      for(int var3 = 0; var3 < 2; ++var3) {
         Buffer var4 = new Buffer();
         var4.setData(new byte[var2]);

         try {
            this.emptyQueue.put(var4);
         } catch (InterruptedException var6) {
            throw new RuntimeException(var6);
         }
      }

      InputStreamReader.ReaderThread var7 = new InputStreamReader.ReaderThread(this.emptyQueue, this.fullQueue, var1, var2);
      this.readerThread = var7;
      StringBuilder var5 = new StringBuilder();
      var5.append("ReaderThread for ");
      var5.append(var1);
      var7.setName(var5.toString());
      this.readerThread.setDaemon(true);
   }

   public int available() throws IOException {
      IOException var1 = this.readException;
      if (var1 == null) {
         Buffer var2 = this.readBuffer;
         return var2 != null && var2.getLength() > 0 ? this.readBuffer.getLength() : 0;
      } else {
         throw var1;
      }
   }

   public void close() throws IOException {
      super.close();
      InputStreamReader.ReaderThread var1 = this.readerThread;
      if (var1 != null) {
         var1.close();
         this.readerThread = null;
      }

   }

   public int read() throws IOException {
      byte[] var1 = new byte[1];
      return this.read(var1, 0, 1) == -1 ? -1 : var1[0] & 255;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      boolean var10001;
      label72: {
         Object var4;
         try {
            if (this.readBuffer != null || this.readException != null) {
               break label72;
            }

            var4 = this.fullQueue.get();
            if (var4 instanceof IOException) {
               this.readException = (IOException)var4;
               break label72;
            }
         } catch (InterruptedException var10) {
            var10001 = false;
            throw new InterruptedIOException();
         }

         try {
            this.readBuffer = (Buffer)var4;
         } catch (InterruptedException var9) {
            var10001 = false;
            throw new InterruptedIOException();
         }
      }

      label58: {
         try {
            if (this.readException == null) {
               if (this.readBuffer.isEOM()) {
                  return -1;
               }
               break label58;
            }
         } catch (InterruptedException var8) {
            var10001 = false;
            throw new InterruptedIOException();
         }

         try {
            throw this.readException;
         } catch (InterruptedException var7) {
            var10001 = false;
            throw new InterruptedIOException();
         }
      }

      byte[] var11;
      try {
         var11 = (byte[])((byte[])this.readBuffer.getData());
         if (this.readBuffer.getLength() < var3) {
            var3 = this.readBuffer.getLength();
         }
      } catch (InterruptedException var6) {
         var10001 = false;
         throw new InterruptedIOException();
      }

      try {
         System.arraycopy(var11, this.readBuffer.getOffset(), var1, var2, var3);
         this.readBuffer.setOffset(this.readBuffer.getOffset() + var3);
         this.readBuffer.setLength(this.readBuffer.getLength() - var3);
         if (this.readBuffer.getLength() == 0) {
            this.emptyQueue.put(this.readBuffer);
            this.readBuffer = null;
            return var3;
         } else {
            return var3;
         }
      } catch (InterruptedException var5) {
         var10001 = false;
         throw new InterruptedIOException();
      }
   }

   public void startReaderThread() {
      if (!this.readerThreadStarted) {
         this.readerThread.start();
         this.readerThreadStarted = true;
      }

   }

   private static class ReaderThread extends CloseableThread {
      private final int bufferSize;
      private final ProducerConsumerQueue emptyQueue;
      private final ProducerConsumerQueue fullQueue;
      // $FF: renamed from: is java.io.InputStream
      private final InputStream field_15;

      public ReaderThread(ProducerConsumerQueue var1, ProducerConsumerQueue var2, InputStream var3, int var4) {
         this.emptyQueue = var1;
         this.fullQueue = var2;
         this.field_15 = var3;
         this.bufferSize = var4;
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }
}
