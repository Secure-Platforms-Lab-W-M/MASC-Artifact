package net.sf.fmj.codegen;

import com.sun.media.format.WavAudioFormat;
import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.format.H261Format;
import javax.media.format.H263Format;
import javax.media.format.IndexedColorFormat;
import javax.media.format.JPEGFormat;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.FileTypeDescriptor;
import org.atalk.android.util.java.awt.Dimension;

public class MediaCGUtils {
   public static String dataTypeToStr(Class var0) {
      if (var0 == null) {
         return "null";
      } else if (var0 == Format.byteArray) {
         return "Format.byteArray";
      } else if (var0 == Format.shortArray) {
         return "Format.shortArray";
      } else if (var0 == Format.intArray) {
         return "Format.intArray";
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static String formatToStr(Format var0) {
      if (var0 == null) {
         return "null";
      } else {
         Class var1 = var0.getClass();
         StringBuilder var6;
         if (var1 == RGBFormat.class) {
            RGBFormat var13 = (RGBFormat)var0;
            var6 = new StringBuilder();
            var6.append("new RGBFormat(");
            var6.append(toLiteral(var13.getSize()));
            var6.append(", ");
            var6.append(var13.getMaxDataLength());
            var6.append(", ");
            var6.append(dataTypeToStr(var13.getDataType()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var13.getFrameRate()));
            var6.append(", ");
            var6.append(var13.getBitsPerPixel());
            var6.append(", ");
            var6.append(CGUtils.toHexLiteral(var13.getRedMask()));
            var6.append(", ");
            var6.append(CGUtils.toHexLiteral(var13.getGreenMask()));
            var6.append(", ");
            var6.append(CGUtils.toHexLiteral(var13.getBlueMask()));
            var6.append(", ");
            var6.append(var13.getPixelStride());
            var6.append(", ");
            var6.append(var13.getLineStride());
            var6.append(", ");
            var6.append(var13.getFlipped());
            var6.append(", ");
            var6.append(var13.getEndian());
            var6.append(")");
            return var6.toString();
         } else if (var1 == YUVFormat.class) {
            YUVFormat var12 = (YUVFormat)var0;
            var6 = new StringBuilder();
            var6.append("new YUVFormat(");
            var6.append(toLiteral(var12.getSize()));
            var6.append(", ");
            var6.append(var12.getMaxDataLength());
            var6.append(", ");
            var6.append(dataTypeToStr(var12.getDataType()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var12.getFrameRate()));
            var6.append(", ");
            var6.append(var12.getYuvType());
            var6.append(", ");
            var6.append(var12.getStrideY());
            var6.append(", ");
            var6.append(var12.getStrideUV());
            var6.append(", ");
            var6.append(var12.getOffsetY());
            var6.append(", ");
            var6.append(var12.getOffsetU());
            var6.append(", ");
            var6.append(var12.getOffsetV());
            var6.append(")");
            return var6.toString();
         } else if (var1 == JPEGFormat.class) {
            JPEGFormat var11 = (JPEGFormat)var0;
            var6 = new StringBuilder();
            var6.append("new JPEGFormat(");
            var6.append(toLiteral(var11.getSize()));
            var6.append(", ");
            var6.append(var11.getMaxDataLength());
            var6.append(", ");
            var6.append(dataTypeToStr(var11.getDataType()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var11.getFrameRate()));
            var6.append(", ");
            var6.append(var11.getQFactor());
            var6.append(", ");
            var6.append(var11.getDecimation());
            var6.append(")");
            return var6.toString();
         } else if (var1 == IndexedColorFormat.class) {
            IndexedColorFormat var10 = (IndexedColorFormat)var0;
            var6 = new StringBuilder();
            var6.append("new IndexedColorFormat(");
            var6.append(toLiteral(var10.getSize()));
            var6.append(", ");
            var6.append(var10.getMaxDataLength());
            var6.append(", ");
            var6.append(dataTypeToStr(var10.getDataType()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var10.getFrameRate()));
            var6.append(", ");
            var6.append(var10.getLineStride());
            var6.append(", ");
            var6.append(var10.getMapSize());
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var10.getRedValues()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var10.getGreenValues()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var10.getBlueValues()));
            var6.append(")");
            return var6.toString();
         } else if (var1 == H263Format.class) {
            H263Format var9 = (H263Format)var0;
            var6 = new StringBuilder();
            var6.append("new H263Format(");
            var6.append(toLiteral(var9.getSize()));
            var6.append(", ");
            var6.append(var9.getMaxDataLength());
            var6.append(", ");
            var6.append(dataTypeToStr(var9.getDataType()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var9.getFrameRate()));
            var6.append(", ");
            var6.append(var9.getAdvancedPrediction());
            var6.append(", ");
            var6.append(var9.getArithmeticCoding());
            var6.append(", ");
            var6.append(var9.getErrorCompensation());
            var6.append(", ");
            var6.append(var9.getHrDB());
            var6.append(", ");
            var6.append(var9.getPBFrames());
            var6.append(", ");
            var6.append(var9.getUnrestrictedVector());
            var6.append(")");
            return var6.toString();
         } else if (var1 == H261Format.class) {
            H261Format var8 = (H261Format)var0;
            var6 = new StringBuilder();
            var6.append("new H261Format(");
            var6.append(toLiteral(var8.getSize()));
            var6.append(", ");
            var6.append(var8.getMaxDataLength());
            var6.append(", ");
            var6.append(dataTypeToStr(var8.getDataType()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var8.getFrameRate()));
            var6.append(", ");
            var6.append(var8.getStillImageTransmission());
            var6.append(")");
            return var6.toString();
         } else if (var1 == AudioFormat.class) {
            AudioFormat var7 = (AudioFormat)var0;
            var6 = new StringBuilder();
            var6.append("new AudioFormat(");
            var6.append(CGUtils.toLiteral(var7.getEncoding()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var7.getSampleRate()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var7.getSampleSizeInBits()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var7.getChannels()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var7.getEndian()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var7.getSigned()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var7.getFrameSizeInBits()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var7.getFrameRate()));
            var6.append(", ");
            var6.append(dataTypeToStr(var7.getDataType()));
            var6.append(")");
            return var6.toString();
         } else if (var1 == VideoFormat.class) {
            VideoFormat var5 = (VideoFormat)var0;
            var6 = new StringBuilder();
            var6.append("new VideoFormat(");
            var6.append(CGUtils.toLiteral(var5.getEncoding()));
            var6.append(", ");
            var6.append(toLiteral(var5.getSize()));
            var6.append(", ");
            var6.append(var5.getMaxDataLength());
            var6.append(", ");
            var6.append(dataTypeToStr(var5.getDataType()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var5.getFrameRate()));
            var6.append(")");
            return var6.toString();
         } else if (var1 == Format.class) {
            var6 = new StringBuilder();
            var6.append("new Format(");
            var6.append(CGUtils.toLiteral(var0.getEncoding()));
            var6.append(", ");
            var6.append(dataTypeToStr(var0.getDataType()));
            var6.append(")");
            return var6.toString();
         } else if (var1 == FileTypeDescriptor.class) {
            FileTypeDescriptor var4 = (FileTypeDescriptor)var0;
            var6 = new StringBuilder();
            var6.append("new FileTypeDescriptor(");
            var6.append(CGUtils.toLiteral(var4.getEncoding()));
            var6.append(")");
            return var6.toString();
         } else if (var1 == ContentDescriptor.class) {
            ContentDescriptor var3 = (ContentDescriptor)var0;
            var6 = new StringBuilder();
            var6.append("new ContentDescriptor(");
            var6.append(CGUtils.toLiteral(var3.getEncoding()));
            var6.append(")");
            return var6.toString();
         } else if (var1 == WavAudioFormat.class) {
            WavAudioFormat var2 = (WavAudioFormat)var0;
            var6 = new StringBuilder();
            var6.append("new com.sun.media.format.WavAudioFormat(");
            var6.append(CGUtils.toLiteral(var2.getEncoding()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var2.getSampleRate()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var2.getSampleSizeInBits()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var2.getChannels()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var2.getFrameSizeInBits()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var2.getAverageBytesPerSecond()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var2.getEndian()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var2.getSigned()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral((float)var2.getFrameRate()));
            var6.append(", ");
            var6.append(dataTypeToStr(var2.getDataType()));
            var6.append(", ");
            var6.append(CGUtils.toLiteral(var2.getCodecSpecificHeader()));
            var6.append(")");
            return var6.toString();
         } else {
            var6 = new StringBuilder();
            var6.append("");
            var6.append(var0.getClass());
            throw new IllegalArgumentException(var6.toString());
         }
      }
   }

   public static String toLiteral(Dimension var0) {
      if (var0 == null) {
         return "null";
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("new java.awt.Dimension(");
         var1.append(var0.width);
         var1.append(", ");
         var1.append(var0.height);
         var1.append(")");
         return var1.toString();
      }
   }
}
