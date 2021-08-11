package net.sf.fmj.media.multiplexer.audio;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.AudioFormat;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.Seekable;
import net.sf.fmj.media.codec.JavaSoundCodec;
import net.sf.fmj.media.multiplexer.AbstractInputStreamMux;
import net.sf.fmj.media.multiplexer.InputStreamPushDataSource;
import net.sf.fmj.media.multiplexer.InputStreamPushSourceStream;
import net.sf.fmj.media.renderer.audio.JavaSoundUtils;
import net.sf.fmj.utility.LoggerSingleton;

public class AUMux extends AbstractInputStreamMux {
   private static final Logger logger;
   private long bytesWritten;
   private boolean headerWritten = false;
   private boolean trailerWritten = false;

   static {
      logger = LoggerSingleton.logger;
   }

   public AUMux() {
      super(new FileTypeDescriptor("audio.basic"));
   }

   private void outputHeader(OutputStream var1) throws IOException {
      byte[] var2 = JavaSoundCodec.createAuHeader(JavaSoundUtils.convertFormat((AudioFormat)this.inputFormats[0]));
      if (var2 != null) {
         var1.write(var2);
      } else {
         throw new IOException("Unable to create AU header");
      }
   }

   private void outputTrailer(OutputStream var1) throws IOException {
      DataSource var2 = this.getDataOutput();
      if (var2 instanceof InputStreamPushDataSource) {
         PushSourceStream var3 = ((InputStreamPushDataSource)var2).getStreams()[0];
         if (var3 instanceof InputStreamPushSourceStream) {
            InputStreamPushSourceStream var4 = (InputStreamPushSourceStream)var3;
            if ((Seekable)var4.getTransferHandler() instanceof Seekable) {
               ((Seekable)var4.getTransferHandler()).seek(8L);
               this.writeInt(var1, this.bytesWritten);
               if (this.getDataOutputNoInit() != null) {
                  this.getDataOutputNoInit().notifyDataAvailable(0);
               }
            }
         }
      }

   }

   public void close() {
      if (!this.trailerWritten) {
         try {
            this.outputTrailer(this.getOutputStream());
         } catch (IOException var5) {
            Logger var2 = logger;
            Level var3 = Level.WARNING;
            StringBuilder var4 = new StringBuilder();
            var4.append("");
            var4.append(var5);
            var2.log(var3, var4.toString(), var5);
            throw new RuntimeException(var5);
         }

         this.trailerWritten = true;
      }

      super.close();
   }

   protected void doProcess(Buffer var1, int var2, OutputStream var3) throws IOException {
      if (!this.headerWritten) {
         this.outputHeader(var3);
         this.headerWritten = true;
      }

      if (var1.isEOM()) {
         if (!this.trailerWritten) {
            this.outputTrailer(var3);
            this.trailerWritten = true;
         }

         var3.close();
      } else if (!var1.isDiscard()) {
         var3.write((byte[])((byte[])var1.getData()), var1.getOffset(), var1.getLength());
         this.bytesWritten += (long)var1.getLength();
      }
   }

   public Format[] getSupportedInputFormats() {
      return new Format[]{new AudioFormat("LINEAR", -1.0D, 8, -1, -1, 1), new AudioFormat("LINEAR", -1.0D, 16, -1, 1, 1), new AudioFormat("LINEAR", -1.0D, 24, -1, 1, 1), new AudioFormat("LINEAR", -1.0D, 32, -1, 1, 1), new AudioFormat("ULAW"), new AudioFormat("alaw")};
   }

   public void open() throws ResourceUnavailableException {
      super.open();
      if (!this.headerWritten) {
         try {
            this.outputHeader(this.getOutputStream());
         } catch (IOException var5) {
            Logger var2 = logger;
            Level var3 = Level.WARNING;
            StringBuilder var4 = new StringBuilder();
            var4.append("");
            var4.append(var5);
            var2.log(var3, var4.toString(), var5);
            StringBuilder var6 = new StringBuilder();
            var6.append("");
            var6.append(var5);
            throw new ResourceUnavailableException(var6.toString());
         }

         this.headerWritten = true;
      }
   }

   public Format setInputFormat(Format var1, int var2) {
      Logger var7 = logger;
      StringBuilder var8 = new StringBuilder();
      var8.append("setInputFormat ");
      var8.append(var1);
      var8.append(" ");
      var8.append(var2);
      var7.finer(var8.toString());
      boolean var5 = false;
      Format[] var9 = this.getSupportedInputFormats();
      int var6 = var9.length;
      int var3 = 0;

      boolean var4;
      while(true) {
         var4 = var5;
         if (var3 >= var6) {
            break;
         }

         if (var1.matches(var9[var3])) {
            var4 = true;
            break;
         }

         ++var3;
      }

      if (!var4) {
         var7 = logger;
         var8 = new StringBuilder();
         var8.append("Input format does not match any supported input format: ");
         var8.append(var1);
         var7.warning(var8.toString());
         return null;
      } else {
         if (this.inputFormats != null) {
            this.inputFormats[var2] = var1;
         }

         return var1;
      }
   }
}
