package net.sf.fmj.media.multiplexer;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import net.sf.fmj.media.AbstractMultiplexer;
import net.sf.fmj.media.BufferQueueInputStream;
import net.sf.fmj.utility.LoggerSingleton;

public abstract class AbstractStreamCopyMux extends AbstractMultiplexer {
   private static final Logger logger;
   private BufferQueueInputStream[] bufferQueueInputStreams;
   private final ContentDescriptor contentDescriptor;
   private StreamCopyPushDataSource dataOutput;

   static {
      logger = LoggerSingleton.logger;
   }

   public AbstractStreamCopyMux(ContentDescriptor var1) {
      this.contentDescriptor = var1;
   }

   public void close() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" close");
      var1.finer(var2.toString());
      super.close();
      StreamCopyPushDataSource var6 = this.dataOutput;
      if (var6 != null) {
         try {
            var6.stop();
         } catch (IOException var5) {
            Logger var7 = logger;
            Level var3 = Level.WARNING;
            StringBuilder var4 = new StringBuilder();
            var4.append("");
            var4.append(var5);
            var7.log(var3, var4.toString(), var5);
         }

         this.dataOutput.disconnect();
      }

   }

   protected StreamCopyPushDataSource createInputStreamPushDataSource(ContentDescriptor var1, int var2, InputStream[] var3, Format[] var4) {
      return new StreamCopyPushDataSource(var1, var2, var3, var4);
   }

   public DataSource getDataOutput() {
      if (this.dataOutput == null) {
         this.dataOutput = this.createInputStreamPushDataSource(this.outputContentDescriptor, this.numTracks, this.bufferQueueInputStreams, this.inputFormats);
      }

      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" getDataOutput");
      var1.finer(var2.toString());
      return this.dataOutput;
   }

   public abstract Format[] getSupportedInputFormats();

   public ContentDescriptor[] getSupportedOutputContentDescriptors(Format[] var1) {
      return new ContentDescriptor[]{this.contentDescriptor};
   }

   public void open() throws ResourceUnavailableException {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" open");
      var1.finer(var2.toString());
      super.open();
   }

   public int process(Buffer var1, int var2) {
      Logger var3 = logger;
      StringBuilder var4 = new StringBuilder();
      var4.append(this.getClass().getSimpleName());
      var4.append(" process ");
      var4.append(var1);
      var4.append(" ");
      var4.append(var2);
      var4.append(" length ");
      var4.append(var1.getLength());
      var3.finer(var4.toString());
      if (var1.isEOM()) {
         var3 = logger;
         var4 = new StringBuilder();
         var4.append("processing EOM buffer for track: ");
         var4.append(var2);
         var3.finer(var4.toString());
      }

      if (!this.bufferQueueInputStreams[var2].put(var1)) {
         return 2;
      } else {
         try {
            if (var1.isEOM()) {
               logger.fine("EOM, waitUntilFinished...");
               if (this.dataOutput != null) {
                  this.dataOutput.waitUntilFinished();
               }

               logger.fine("EOM, finished.");
            }

            if (this.dataOutput != null) {
               this.dataOutput.notifyDataAvailable(var2);
            }

            return 0;
         } catch (InterruptedException var6) {
            var3 = logger;
            Level var7 = Level.WARNING;
            StringBuilder var5 = new StringBuilder();
            var5.append("");
            var5.append(var6);
            var3.log(var7, var5.toString(), var6);
            return 1;
         }
      }
   }

   public int setNumTracks(int var1) {
      int var2 = super.setNumTracks(var1);
      this.bufferQueueInputStreams = new BufferQueueInputStream[var2];

      for(var1 = 0; var1 < var2; ++var1) {
         this.bufferQueueInputStreams[var1] = new BufferQueueInputStream();
      }

      return var2;
   }
}
