package net.sf.fmj.media.multiplexer;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.media.Time;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushDataSource;
import javax.media.protocol.PushSourceStream;
import net.sf.fmj.utility.LoggerSingleton;

public class InputStreamPushDataSource extends PushDataSource {
   private static final Logger logger;
   private final InputStream[] inputStreams;
   private final int numTracks;
   private final ContentDescriptor outputContentDescriptor;
   private InputStreamPushSourceStream[] pushSourceStreams;

   static {
      logger = LoggerSingleton.logger;
   }

   public InputStreamPushDataSource(ContentDescriptor var1, int var2, InputStream[] var3) {
      this.outputContentDescriptor = var1;
      this.numTracks = var2;
      this.inputStreams = var3;
   }

   public void connect() throws IOException {
      Logger var2 = logger;
      StringBuilder var3 = new StringBuilder();
      var3.append(this.getClass().getSimpleName());
      var3.append(" connect");
      var2.finer(var3.toString());
      this.pushSourceStreams = new InputStreamPushSourceStream[this.numTracks];

      for(int var1 = 0; var1 < this.numTracks; ++var1) {
         this.pushSourceStreams[var1] = new InputStreamPushSourceStream(this.outputContentDescriptor, this.inputStreams[var1]);
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
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" start");
      var1.finer(var2.toString());
   }

   public void stop() throws IOException {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" stop");
      var1.finer(var2.toString());
   }
}
