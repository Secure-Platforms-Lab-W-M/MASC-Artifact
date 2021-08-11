package net.sf.fmj.media.codec.video;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.AbstractCodec;
import net.sf.fmj.media.util.BufferToImage;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.image.BufferedImage;
import org.atalk.android.util.javax.imageio.ImageIO;

public abstract class ImageIOEncoder extends AbstractCodec implements Codec {
   private BufferToImage bufferToImage;
   private final String formatName;
   private final Format[] supportedInputFormats;

   public ImageIOEncoder(String var1) {
      this.supportedInputFormats = new Format[]{new RGBFormat((Dimension)null, -1, Format.byteArray, -1.0F, -1, -1, -1, -1), new RGBFormat((Dimension)null, -1, Format.intArray, -1.0F, -1, -1, -1, -1)};
      this.formatName = var1;
      if (!ImageIO.getImageWritersByFormatName(var1).hasNext()) {
         StringBuilder var2 = new StringBuilder();
         var2.append("No ImageIO writer found for ");
         var2.append(var1);
         throw new RuntimeException(var2.toString());
      }
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputFormats;
   }

   public int process(Buffer var1, Buffer var2) {
      if (!this.checkInputBuffer(var1)) {
         return 1;
      } else if (this.isEOM(var1)) {
         this.propagateEOM(var2);
         return 0;
      } else {
         BufferedImage var5 = (BufferedImage)this.bufferToImage.createImage(var1);

         try {
            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            if (ImageIO.write(var5, this.formatName, var3)) {
               var3.close();
               byte[] var7 = var3.toByteArray();
               var2.setData(var7);
               var2.setOffset(0);
               var2.setLength(var7.length);
               return 0;
            } else {
               StringBuilder var6 = new StringBuilder();
               var6.append("No ImageIO writer found for ");
               var6.append(this.formatName);
               throw new RuntimeException(var6.toString());
            }
         } catch (IOException var4) {
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
