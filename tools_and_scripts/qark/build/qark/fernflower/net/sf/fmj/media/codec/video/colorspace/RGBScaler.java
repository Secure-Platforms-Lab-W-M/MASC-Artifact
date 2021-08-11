package net.sf.fmj.media.codec.video.colorspace;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.BasicCodec;
import org.atalk.android.util.java.awt.Dimension;

public class RGBScaler extends BasicCodec {
   private static boolean nativeAvailable = false;
   private int nativeData;
   protected float quality;

   public RGBScaler() {
      this((Dimension)null);
   }

   public RGBScaler(Dimension var1) {
      this.quality = 0.5F;
      this.nativeData = 0;
      this.inputFormats = new Format[]{new RGBFormat((Dimension)null, -1, Format.byteArray, -1.0F, 24, 3, 2, 1, 3, -1, 0, -1)};
      if (var1 != null) {
         this.setOutputSize(var1);
      }

   }

   private native void nativeClose();

   private native void nativeScale(Object var1, long var2, Object var4, long var5, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14);

   public void close() {
      super.close();
      if (nativeAvailable && this.nativeData != 0) {
         try {
            this.nativeClose();
         } finally {
            return;
         }
      }
   }

   public String getName() {
      return "RGB Scaler";
   }

   public Format[] getSupportedOutputFormats(Format var1) {
      if (var1 == null) {
         return this.outputFormats;
      } else if (matches(var1, this.inputFormats) != null) {
         VideoFormat var2 = new VideoFormat((String)null, (Dimension)null, -1, (Class)null, ((VideoFormat)var1).getFrameRate());
         return new Format[]{this.outputFormats[0].intersects(var2)};
      } else {
         return new Format[0];
      }
   }

   protected void nearestNeighbour(Buffer param1, Buffer param2) {
      // $FF: Couldn't be decompiled
   }

   public int process(Buffer var1, Buffer var2) {
      var2.setLength(((VideoFormat)this.outputFormat).getMaxDataLength());
      var2.setFormat(this.outputFormat);
      if (this.quality <= 0.5F) {
         this.nearestNeighbour(var1, var2);
      }

      return 0;
   }

   public Format setInputFormat(Format var1) {
      return matches(var1, this.inputFormats) == null ? null : var1;
   }

   public Format setOutputFormat(Format var1) {
      if (var1 != null) {
         if (matches(var1, this.outputFormats) == null) {
            return null;
         } else {
            RGBFormat var7 = (RGBFormat)var1;
            Dimension var6 = var7.getSize();
            int var3 = var7.getMaxDataLength();
            int var4 = var7.getLineStride();
            float var2 = var7.getFrameRate();
            int var5 = var7.getFlipped();
            var7.getEndian();
            if (var6 == null) {
               return null;
            } else {
               if (var3 < var6.width * var6.height * 3) {
                  var3 = var6.width * var6.height * 3;
               }

               if (var4 < var6.width * 3) {
                  var4 = var6.width * 3;
               }

               if (var5 != 0) {
               }

               this.outputFormat = this.outputFormats[0].intersects(new RGBFormat(var6, var3, (Class)null, var2, -1, -1, -1, -1, -1, var4, -1, -1));
               return this.outputFormat;
            }
         }
      } else {
         return null;
      }
   }

   public void setOutputSize(Dimension var1) {
      this.outputFormats = new Format[]{new RGBFormat(var1, var1.width * var1.height * 3, Format.byteArray, -1.0F, 24, 3, 2, 1, 3, var1.width * 3, 0, -1)};
   }
}
