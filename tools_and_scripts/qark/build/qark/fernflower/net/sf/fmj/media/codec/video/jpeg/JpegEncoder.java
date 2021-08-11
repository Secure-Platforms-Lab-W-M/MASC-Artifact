package net.sf.fmj.media.codec.video.jpeg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import javax.media.format.JPEGFormat;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.AbstractCodec;
import net.sf.fmj.media.util.BufferToImage;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.image.BufferedImage;
import org.atalk.android.util.javax.imageio.IIOImage;
import org.atalk.android.util.javax.imageio.ImageIO;
import org.atalk.android.util.javax.imageio.ImageWriter;
import org.atalk.android.util.javax.imageio.metadata.IIOMetadata;
import org.atalk.android.util.javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import org.atalk.android.util.javax.imageio.stream.MemoryCacheImageOutputStream;

public class JpegEncoder extends AbstractCodec implements Codec {
   private BufferToImage bufferToImage;
   private final Format[] supportedInputFormats;
   private final Format[] supportedOutputFormats;

   public JpegEncoder() {
      this.supportedInputFormats = new Format[]{new RGBFormat((Dimension)null, -1, Format.byteArray, -1.0F, -1, -1, -1, -1), new RGBFormat((Dimension)null, -1, Format.intArray, -1.0F, -1, -1, -1, -1)};
      this.supportedOutputFormats = new Format[]{new JPEGFormat()};
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputFormats;
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.supportedOutputFormats;
      } else {
         VideoFormat var2 = (VideoFormat)var1;
         return new Format[]{new JPEGFormat(var2.getSize(), -1, Format.byteArray, var2.getFrameRate(), -1, -1)};
      }
   }

   public int process(Buffer var1, Buffer var2) {
      if (!this.checkInputBuffer(var1)) {
         return 1;
      } else if (this.isEOM(var1)) {
         this.propagateEOM(var2);
         return 0;
      } else {
         BufferedImage var8 = (BufferedImage)this.bufferToImage.createImage(var1);

         try {
            JPEGImageWriteParam var3 = new JPEGImageWriteParam((Locale)null);
            var3.setCompressionMode(2);
            var3.setCompressionQuality(0.74F);
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            MemoryCacheImageOutputStream var5 = new MemoryCacheImageOutputStream(var4);
            ImageWriter var6 = (ImageWriter)ImageIO.getImageWritersByFormatName("JPEG").next();
            var6.setOutput(var5);
            var6.write((IIOMetadata)null, new IIOImage(var8, (List)null, (IIOMetadata)null), var3);
            var5.close();
            var4.close();
            byte[] var9 = var4.toByteArray();
            var2.setData(var9);
            var2.setOffset(0);
            var2.setLength(var9.length);
            return 0;
         } catch (IOException var7) {
            var2.setDiscard(true);
            var2.setLength(0);
            return 1;
         }
      }
   }

   public Format setInputFormat(Format var1) {
      if (((VideoFormat)var1).getSize() == null) {
         return null;
      } else {
         this.bufferToImage = new BufferToImage((VideoFormat)var1);
         return super.setInputFormat(var1);
      }
   }
}
