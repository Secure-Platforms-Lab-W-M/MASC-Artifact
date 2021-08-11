package net.sf.fmj.media.multiplexer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.JPEGFormat;
import javax.media.protocol.ContentDescriptor;
import net.sf.fmj.media.format.GIFFormat;
import net.sf.fmj.media.format.PNGFormat;
import net.sf.fmj.utility.LoggerSingleton;

public class MultipartMixedReplaceMux extends AbstractInputStreamMux {
   public static final String BOUNDARY = "--ssBoundaryFMJ";
   private static final int MAX_TRACKS = 1;
   public static final String TIMESTAMP_KEY = "X-FMJ-Timestamp";
   private static final Logger logger;

   static {
      logger = LoggerSingleton.logger;
   }

   public MultipartMixedReplaceMux() {
      super(new ContentDescriptor("multipart.x_mixed_replace"));
   }

   protected void doProcess(Buffer var1, int var2, OutputStream var3) throws IOException {
      if (var1.isEOM()) {
         var3.close();
      } else if (!var1.isDiscard()) {
         var3.write("--ssBoundaryFMJ\n".getBytes());
         StringBuilder var4 = new StringBuilder();
         var4.append("Content-Type: image/");
         var4.append(var1.getFormat().getEncoding());
         var4.append("\n");
         var3.write(var4.toString().getBytes());
         var4 = new StringBuilder();
         var4.append("Content-Length: ");
         var4.append(var1.getLength());
         var4.append("\n");
         var3.write(var4.toString().getBytes());
         var4 = new StringBuilder();
         var4.append("X-FMJ-Timestamp: ");
         var4.append(var1.getTimeStamp());
         var4.append("\n");
         var3.write(var4.toString().getBytes());
         var3.write("\n".getBytes());
         var3.write((byte[])((byte[])var1.getData()), var1.getOffset(), var1.getLength());
         var3.write("\n\n".getBytes());
      }
   }

   public Format[] getSupportedInputFormats() {
      return new Format[]{new JPEGFormat(), new GIFFormat(), new PNGFormat()};
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

   public int setNumTracks(int var1) {
      byte var2 = 1;
      if (var1 > 1) {
         var1 = var2;
      }

      return super.setNumTracks(var1);
   }
}
