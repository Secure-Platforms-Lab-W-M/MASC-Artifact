package net.sf.fmj.media.codec.video;

import java.io.PrintStream;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.AbstractCodec;
import net.sf.fmj.media.util.BufferToImage;
import net.sf.fmj.media.util.ImageToBuffer;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.geom.AffineTransform;
import org.atalk.android.util.java.awt.image.AffineTransformOp;
import org.atalk.android.util.java.awt.image.BufferedImage;

public class ImageScaler extends AbstractCodec implements Codec {
   private final Dimension DIMENSION = null;
   private BufferToImage bufferToImage;
   private final Format[] supportedInputFormats;
   private final Format[] supportedOutputFormats;

   public ImageScaler() {
      this.supportedInputFormats = new Format[]{new RGBFormat((Dimension)null, -1, Format.byteArray, -1.0F, -1, -1, -1, -1), new RGBFormat((Dimension)null, -1, Format.intArray, -1.0F, -1, -1, -1, -1)};
      this.supportedOutputFormats = new Format[]{new RGBFormat(this.DIMENSION, -1, Format.intArray, -1.0F, -1, -1, -1, -1)};
   }

   private BufferedImage scale(BufferedImage var1, double var2, double var4) {
      AffineTransform var6 = new AffineTransform();
      var6.scale(var2, var4);
      return (new AffineTransformOp(var6, 3)).filter(var1, (BufferedImage)null);
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputFormats;
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.supportedOutputFormats;
      } else {
         VideoFormat var2 = (VideoFormat)var1;
         return new Format[]{new RGBFormat(this.DIMENSION, -1, Format.intArray, -1.0F, -1, -1, -1, -1)};
      }
   }

   public int process(Buffer var1, Buffer var2) {
      if (!this.checkInputBuffer(var1)) {
         return 1;
      } else if (this.isEOM(var1)) {
         this.propagateEOM(var2);
         return 0;
      } else {
         BufferedImage var5 = (BufferedImage)this.bufferToImage.createImage(var1);
         Dimension var3 = ((VideoFormat)this.inputFormat).getSize();
         Dimension var4 = ((VideoFormat)this.outputFormat).getSize();
         var5 = this.scale(var5, (double)var4.width / (double)var3.width, (double)var4.height / (double)var3.height);
         PrintStream var6 = System.out;
         StringBuilder var7 = new StringBuilder();
         var7.append("scaled: ");
         var7.append(var5.getWidth());
         var7.append("x");
         var7.append(var5.getHeight());
         var6.println(var7.toString());
         var1 = ImageToBuffer.createBuffer(var5, ((VideoFormat)this.outputFormat).getFrameRate());
         var2.setData(var1.getData());
         var2.setLength(var1.getLength());
         var2.setOffset(var1.getOffset());
         var2.setFormat(var1.getFormat());
         return 0;
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
