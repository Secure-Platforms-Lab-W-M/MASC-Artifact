package net.sf.fmj.media.codec.video;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.AbstractCodec;
import net.sf.fmj.media.util.ImageToBuffer;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.image.BufferedImage;
import org.atalk.android.util.javax.imageio.ImageIO;

public abstract class ImageIODecoder extends AbstractCodec implements Codec {
   private final Format[] supportedOutputFormats;

   public ImageIODecoder(String var1) {
      this.supportedOutputFormats = new Format[]{new RGBFormat((Dimension)null, -1, Format.byteArray, -1.0F, -1, -1, -1, -1)};
      if (!ImageIO.getImageReadersByFormatName(var1).hasNext()) {
         StringBuilder var2 = new StringBuilder();
         var2.append("No ImageIO reader found for ");
         var2.append(var1);
         throw new RuntimeException(var2.toString());
      }
   }

   public abstract Format[] getSupportedInputFormats();

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.supportedOutputFormats;
      } else {
         VideoFormat var2 = (VideoFormat)var1;
         return new Format[]{new RGBFormat(var2.getSize(), -1, Format.byteArray, var2.getFrameRate(), -1, -1, -1, -1)};
      }
   }

   public int process(Buffer var1, Buffer var2) {
      if (!this.checkInputBuffer(var1)) {
         return 1;
      } else if (this.isEOM(var1)) {
         this.propagateEOM(var2);
         return 0;
      } else {
         try {
            ByteArrayInputStream var5 = new ByteArrayInputStream((byte[])((byte[])var1.getData()), var1.getOffset(), var1.getLength());
            BufferedImage var3 = ImageIO.read(var5);
            var5.close();
            var1 = ImageToBuffer.createBuffer(var3, ((VideoFormat)this.outputFormat).getFrameRate());
            var2.setData(var1.getData());
            var2.setOffset(var1.getOffset());
            var2.setLength(var1.getLength());
            var2.setFormat(var1.getFormat());
            return 0;
         } catch (IOException var4) {
            var2.setDiscard(true);
            var2.setLength(0);
            return 1;
         }
      }
   }

   public Format setInputFormat(Format var1) {
      return ((VideoFormat)var1).getSize() == null ? null : super.setInputFormat(var1);
   }
}
