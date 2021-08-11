package net.sf.fmj.media.multiplexer.audio;

import com.lti.utils.UnsignedUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import net.sf.fmj.media.multiplexer.AbstractInputStreamMux;
import net.sf.fmj.utility.FormatArgUtils;
import net.sf.fmj.utility.LoggerSingleton;

public class CsvAudioMux extends AbstractInputStreamMux {
   private static final Logger logger;
   private boolean headerWritten = false;
   private boolean trailerWritten = false;

   static {
      logger = LoggerSingleton.logger;
   }

   public CsvAudioMux() {
      super(new ContentDescriptor("audio.csv"));
   }

   public static void audioBufferToStream(AudioFormat var0, Buffer var1, OutputStream var2) throws IOException {
      byte[] var14 = (byte[])((byte[])var1.getData());
      int var5 = var0.getSampleSizeInBits() / 8;
      if (var5 * 8 != var0.getSampleSizeInBits()) {
         throw new RuntimeException("Sample size in bytes must be divisible by 8");
      } else {
         int var6 = var0.getChannels() * var5;
         int var7 = var1.getLength() / var6;
         if (var1.getLength() != var7 * var6) {
            throw new RuntimeException("Length of buffer not an integral number of samples");
         } else {
            int var8 = var0.getSampleSizeInBits();
            long var10 = 1L;
            int var9 = var0.getSampleSizeInBits();

            for(int var3 = 0; var3 < var7; ++var3) {
               for(int var4 = 0; var4 < var0.getChannels(); ++var4) {
                  long var12 = UnsignedUtils.uIntToLong(getSample(var14, var1.getOffset() + var3 * var6 + var4 * var5, var5, var0.getEndian()));
                  if (var0.getSigned() != 0) {
                     if (var0.getSigned() != 1) {
                        throw new RuntimeException("input format signed not specified");
                     }

                     if (var12 > (1L << var9 - 1) - 1L) {
                        var10 = 1L;
                        var12 = var12 - ((1L << var8) - 1L) - 1L;
                     } else {
                        var10 = 1L;
                     }
                  }

                  if (var4 > 0) {
                     var2.write(",".getBytes());
                  }

                  StringBuilder var15 = new StringBuilder();
                  var15.append("");
                  var15.append(var12);
                  var2.write(var15.toString().getBytes());
               }

               var2.write("\n".getBytes());
            }

         }
      }
   }

   private static int getSample(byte[] var0, int var1, int var2, int var3) {
      int var5 = 0;

      for(int var4 = 0; var4 < var2; ++var4) {
         int var6;
         if (var3 == 1) {
            var6 = var4;
         } else {
            var6 = var2 - 1 - var4;
         }

         var5 = var5 << 8 | var0[var1 + var6] & 255;
      }

      return var5;
   }

   private void outputHeader(OutputStream var1) throws IOException {
      var1.write(FormatArgUtils.toString(this.inputFormats[0]).getBytes());
      var1.write("\n".getBytes());
   }

   private void outputTrailer(OutputStream var1) throws IOException {
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
         audioBufferToStream((AudioFormat)this.inputFormats[0], var1, var3);
      }
   }

   public Format[] getSupportedInputFormats() {
      return new Format[]{new AudioFormat("LINEAR", -1.0D, -1, -1, -1, -1, -1, -1.0D, Format.byteArray)};
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
