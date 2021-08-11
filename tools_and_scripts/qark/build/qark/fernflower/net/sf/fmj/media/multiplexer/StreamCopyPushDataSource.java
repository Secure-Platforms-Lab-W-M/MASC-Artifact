package net.sf.fmj.media.multiplexer;

import com.lti.utils.synchronization.CloseableThread;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Format;
import javax.media.Time;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushDataSource;
import javax.media.protocol.PushSourceStream;
import net.sf.fmj.utility.IOUtils;
import net.sf.fmj.utility.LoggerSingleton;

public class StreamCopyPushDataSource extends PushDataSource {
   private static final Logger logger;
   private final Format[] inputFormats;
   private final InputStream[] inputStreams;
   private final int numTracks;
   private final ContentDescriptor outputContentDescriptor;
   private InputStreamPushSourceStream[] pushSourceStreams;
   private StreamCopyPushDataSource.WriterThread[] writerThreads;

   static {
      logger = LoggerSingleton.logger;
   }

   public StreamCopyPushDataSource(ContentDescriptor var1, int var2, InputStream[] var3, Format[] var4) {
      this.outputContentDescriptor = var1;
      this.numTracks = var2;
      this.inputStreams = var3;
      this.inputFormats = var4;
   }

   public void connect() throws IOException {
      Logger var2 = logger;
      StringBuilder var3 = new StringBuilder();
      var3.append(this.getClass().getSimpleName());
      var3.append(" connect");
      var2.finer(var3.toString());
      int var1 = this.numTracks;
      this.pushSourceStreams = new InputStreamPushSourceStream[var1];
      this.writerThreads = new StreamCopyPushDataSource.WriterThread[var1];

      for(var1 = 0; var1 < this.numTracks; ++var1) {
         StreamPipe var4 = new StreamPipe();
         this.pushSourceStreams[var1] = new InputStreamPushSourceStream(this.outputContentDescriptor, var4.getInputStream());
         this.writerThreads[var1] = new StreamCopyPushDataSource.WriterThread(var1, this.inputStreams[var1], var4.getOutputStream(), this.inputFormats[var1]);
         StreamCopyPushDataSource.WriterThread var5 = this.writerThreads[var1];
         var3 = new StringBuilder();
         var3.append("WriterThread for track ");
         var3.append(var1);
         var5.setName(var3.toString());
         this.writerThreads[var1].setDaemon(true);
      }

   }

   public void disconnect() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" disconnect");
      var1.finer(var2.toString());
   }

   public String getContentType() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" getContentType");
      var1.finer(var2.toString());
      return this.outputContentDescriptor.getContentType();
   }

   public Object getControl(String var1) {
      Logger var3 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" getControl");
      var3.finer(var2.toString());
      return null;
   }

   public Object[] getControls() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" getControls");
      var1.finer(var2.toString());
      return new Object[0];
   }

   public Time getDuration() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" getDuration");
      var1.finer(var2.toString());
      return Time.TIME_UNKNOWN;
   }

   public PushSourceStream[] getStreams() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" getStreams");
      var1.finer(var2.toString());
      return this.pushSourceStreams;
   }

   public void notifyDataAvailable(int var1) {
      this.pushSourceStreams[var1].notifyDataAvailable();
   }

   public void start() throws IOException {
      Logger var2 = logger;
      StringBuilder var3 = new StringBuilder();
      var3.append(this.getClass().getSimpleName());
      var3.append(" start");
      var2.finer(var3.toString());

      for(int var1 = 0; var1 < this.numTracks; ++var1) {
         this.writerThreads[var1].start();
      }

   }

   public void stop() throws IOException {
      Logger var2 = logger;
      StringBuilder var3 = new StringBuilder();
      var3.append(this.getClass().getSimpleName());
      var3.append(" stop");
      var2.finer(var3.toString());

      int var1;
      for(var1 = 0; var1 < this.numTracks; ++var1) {
         this.writerThreads[var1].close();
      }

      var1 = 0;

      while(true) {
         try {
            if (var1 >= this.numTracks) {
               return;
            }

            this.writerThreads[var1].waitUntilClosed();
         } catch (InterruptedException var4) {
            throw new InterruptedIOException();
         }

         ++var1;
      }
   }

   public void waitUntilFinished() throws InterruptedException {
      int var1 = 0;

      while(true) {
         try {
            if (var1 >= this.numTracks) {
               return;
            }

            this.writerThreads[var1].waitUntilClosed();
         } catch (InterruptedException var3) {
            throw var3;
         }

         ++var1;
      }
   }

   protected void write(InputStream var1, OutputStream var2, int var3) throws IOException {
      IOUtils.copyStream(var1, var2);
   }

   private class WriterThread extends CloseableThread {
      private Format format;
      // $FF: renamed from: in java.io.InputStream
      private final InputStream field_16;
      private final OutputStream out;
      private final int trackID;

      public WriterThread(int var2, InputStream var3, OutputStream var4, Format var5) {
         this.trackID = var2;
         this.field_16 = var3;
         this.out = var4;
         this.format = var5;
      }

      public void run() {
         try {
            Logger var2;
            Level var3;
            StringBuilder var4;
            try {
               StreamCopyPushDataSource.this.write(this.field_16, this.out, this.trackID);
               StreamCopyPushDataSource.logger.finer("WriterThread closing output stream");
               this.out.close();
               return;
            } catch (InterruptedIOException var8) {
               var2 = StreamCopyPushDataSource.logger;
               var3 = Level.FINE;
               var4 = new StringBuilder();
               var4.append("");
               var4.append(var8);
               var2.log(var3, var4.toString(), var8);
            } catch (IOException var9) {
               var2 = StreamCopyPushDataSource.logger;
               var3 = Level.WARNING;
               var4 = new StringBuilder();
               var4.append("");
               var4.append(var9);
               var2.log(var3, var4.toString(), var9);
               return;
            }
         } finally {
            this.setClosed();
         }

      }
   }
}
