package net.sf.fmj.media.multiplexer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import net.sf.fmj.media.AbstractMultiplexer;
import net.sf.fmj.utility.LoggerSingleton;

public abstract class AbstractInputStreamMux extends AbstractMultiplexer {
   private static final int PIPE_SIZE = 200000;
   private static final Logger logger;
   private final ContentDescriptor contentDescriptor;
   private InputStreamPushDataSource dataOutput;
   private PipedInputStream pipedInputStream;
   private PipedOutputStream pipedOutputStream;

   static {
      logger = LoggerSingleton.logger;
   }

   public AbstractInputStreamMux(ContentDescriptor var1) {
      this.contentDescriptor = var1;
   }

   public void close() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" close");
      var1.finer(var2.toString());
      super.close();
      InputStreamPushDataSource var6 = this.dataOutput;
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

   protected InputStreamPushDataSource createInputStreamPushDataSource(ContentDescriptor var1, int var2, InputStream[] var3) {
      return new InputStreamPushDataSource(var1, var2, var3);
   }

   protected void doProcess(Buffer var1, int var2, OutputStream var3) throws IOException {
      if (var1.isEOM()) {
         var3.close();
      } else {
         var3.write((byte[])((byte[])var1.getData()), var1.getOffset(), var1.getLength());
      }
   }

   public DataSource getDataOutput() {
      if (this.dataOutput == null) {
         this.dataOutput = this.createInputStreamPushDataSource(this.outputContentDescriptor, 1, new InputStream[]{this.pipedInputStream});
      }

      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getClass().getSimpleName());
      var2.append(" getDataOutput");
      var1.finer(var2.toString());
      return this.dataOutput;
   }

   protected InputStreamPushDataSource getDataOutputNoInit() {
      return this.dataOutput;
   }

   protected OutputStream getOutputStream() {
      return this.pipedOutputStream;
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

      try {
         this.doProcess(var1, var2, this.pipedOutputStream);
      } catch (IOException var6) {
         var3 = logger;
         Level var8 = Level.SEVERE;
         StringBuilder var5 = new StringBuilder();
         var5.append("");
         var5.append(var6);
         var3.log(var8, var5.toString(), var6);
         return 1;
      }

      InputStreamPushDataSource var7 = this.dataOutput;
      if (var7 != null) {
         var7.notifyDataAvailable(0);
      }

      return 0;
   }

   public int setNumTracks(int var1) {
      var1 = super.setNumTracks(var1);

      try {
         this.pipedInputStream = new BigPipedInputStream(200000);
         this.pipedOutputStream = new PipedOutputStream(this.pipedInputStream);
         return var1;
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   protected void writeInt(OutputStream var1, long var2) throws IOException {
      byte[] var4 = new byte[]{(byte)((int)(var2 >> 24 & 255L)), (byte)((int)(var2 >> 16 & 255L)), (byte)((int)(var2 >> 8 & 255L)), (byte)((int)(255L & var2))};
      var1.write(var4, 0, var4.length);
   }
}
