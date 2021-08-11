package net.sf.fmj.media.multiplexer;

import com.lti.utils.StringUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import net.sf.fmj.utility.FormatArgUtils;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.java.awt.Dimension;

public class XmlMovieMux extends AbstractInputStreamMux {
   private static final Logger logger;
   private boolean headerWritten = false;
   private boolean trailerWritten = false;

   static {
      logger = LoggerSingleton.logger;
   }

   public XmlMovieMux() {
      super(new ContentDescriptor("video.xml"));
   }

   private void outputHeader(OutputStream var1) throws IOException {
      var1.write("<?xml version='1.0' encoding='utf-8'?>\n".getBytes());
      var1.write("<XmlMovie version=\"1.0\">\n".getBytes());
      var1.write("<Tracks>\n".getBytes());

      for(int var2 = 0; var2 < this.numTracks; ++var2) {
         StringBuilder var3 = new StringBuilder();
         var3.append("\t<Track index=\"");
         var3.append(var2);
         var3.append("\" format=\"");
         var3.append(StringUtils.replaceSpecialXMLChars(FormatArgUtils.toString(this.inputFormats[var2])));
         var3.append("\"/>\n");
         var1.write(var3.toString().getBytes());
      }

      var1.write("</Tracks>\n".getBytes());
   }

   private void outputTrailer(OutputStream var1) throws IOException {
      var1.write("</XmlMovie>\n".getBytes());
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
         StringBuilder var4 = new StringBuilder();
         var4.append("<Buffer");
         StringBuilder var5 = new StringBuilder();
         var5.append(" track=\"");
         var5.append(var2);
         var5.append("\"");
         var4.append(var5.toString());
         if (var1.getSequenceNumber() != 9223372036854775806L) {
            var5 = new StringBuilder();
            var5.append(" sequenceNumber=\"");
            var5.append(var1.getSequenceNumber());
            var5.append("\"");
            var4.append(var5.toString());
         }

         var5 = new StringBuilder();
         var5.append(" timeStamp=\"");
         var5.append(var1.getTimeStamp());
         var5.append("\"");
         var4.append(var5.toString());
         if (var1.getDuration() >= 0L) {
            var5 = new StringBuilder();
            var5.append(" duration=\"");
            var5.append(var1.getDuration());
            var5.append("\"");
            var4.append(var5.toString());
         }

         if (var1.getFlags() != 0) {
            var5 = new StringBuilder();
            var5.append(" flags=\"");
            var5.append(Integer.toHexString(var1.getFlags()));
            var5.append("\"");
            var4.append(var5.toString());
         }

         if (var1.getFormat() != null && !var1.getFormat().equals(this.inputFormats[var2])) {
            var5 = new StringBuilder();
            var5.append(" format=\"");
            var5.append(StringUtils.replaceSpecialXMLChars(FormatArgUtils.toString(var1.getFormat())));
            var5.append("\"");
            var4.append(var5.toString());
         }

         var4.append(">");
         var4.append("<Data>");
         var4.append(StringUtils.byteArrayToHexString((byte[])((byte[])var1.getData()), var1.getLength(), var1.getOffset()));
         var4.append("</Data>");
         var4.append("</Buffer>\n");
         var3.write(var4.toString().getBytes());
      }
   }

   public Format[] getSupportedInputFormats() {
      return new Format[]{new AudioFormat((String)null, -1.0D, -1, -1, -1, -1, -1, -1.0D, Format.byteArray), new VideoFormat((String)null, (Dimension)null, -1, Format.byteArray, -1.0F)};
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
