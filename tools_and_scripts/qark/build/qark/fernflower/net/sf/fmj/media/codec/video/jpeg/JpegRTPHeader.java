package net.sf.fmj.media.codec.video.jpeg;

import com.lti.utils.UnsignedUtils;
import net.sf.fmj.utility.ArrayUtility;

public class JpegRTPHeader {
   private static final int BITS_PER_BYTE = 8;
   public static final int HEADER_SIZE = 8;
   private static final int MAX_BYTE = 255;
   private static final int MAX_BYTE_PLUS1 = 256;
   private static final int MAX_SIGNED_BYTE = 127;
   private final int fragmentOffset;
   private final byte height;
   // $FF: renamed from: q byte
   private final byte field_232;
   private final byte type;
   private final byte typeSpecific;
   private final byte width;

   public JpegRTPHeader(byte var1, int var2, byte var3, byte var4, byte var5, byte var6) {
      this.typeSpecific = var1;
      this.fragmentOffset = var2;
      this.type = var3;
      this.field_232 = var4;
      this.width = var5;
      this.height = var6;
   }

   public static byte[] createQHeader(int var0, int[] var1, int[] var2) {
      byte[] var5 = new byte[var0 + 4];
      int var4 = 0 + 1;
      var5[0] = 0;
      int var3 = var4 + 1;
      var5[var4] = 0;
      var4 = var3 + 1;
      var5[var3] = (byte)(var0 >> 8 & 255);
      var3 = var4 + 1;
      var5[var4] = (byte)var0;
      if (var0 != 0) {
         int[] var6 = RFC2035.createZigZag(var1);
         int[] var7 = RFC2035.createZigZag(var2);
         System.arraycopy(ArrayUtility.intArrayToByteArray(var6), 0, var5, var3, var1.length);
         var0 = var3 + var1.length;
         System.arraycopy(ArrayUtility.intArrayToByteArray(var7), 0, var5, var0, var2.length);
         var0 = var2.length;
      }

      return var5;
   }

   public static byte[] createRstHeader(int var0, int var1, int var2, int var3) {
      byte[] var6 = new byte[4];
      int var4 = 0 + 1;
      var6[0] = (byte)(var0 >> 8 & 255);
      int var5 = var4 + 1;
      var6[var4] = (byte)var0;
      var6[var5] = (byte)((var1 & 1) << 7);
      var6[var5] |= (byte)((var2 & 1) << 6);
      var6[var5] = (byte)(var6[var5] | (byte)(var3 >> 8 & 255) & 63);
      var6[var5 + 1] = (byte)var3;
      return var6;
   }

   private static void encode3ByteIntBE(int var0, byte[] var1, int var2) {
      byte var4 = 0;
      int var3 = var0;

      for(var0 = var4; var0 < 3; ++var0) {
         int var5 = var3 & 255;
         int var6 = var5;
         if (var5 > 127) {
            var6 = var5 - 256;
         }

         var1[3 - var0 - 1 + var2] = (byte)var6;
         var3 >>= 8;
      }

   }

   public static JpegRTPHeader parse(byte[] var0, int var1) {
      int var5 = var1 + 1;
      byte var2 = var0[var1];
      int var6 = 0;

      for(var1 = 0; var6 < 3; ++var5) {
         var1 = (var1 << 8) + (var0[var5] & 255);
         ++var6;
      }

      var6 = var5 + 1;
      byte var3 = var0[var5];
      var5 = var6 + 1;
      byte var4 = var0[var6];
      var6 = var5 + 1;
      return new JpegRTPHeader(var2, var1, var3, var4, var0[var5], var0[var6]);
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof JpegRTPHeader;
      boolean var3 = false;
      if (!var2) {
         return false;
      } else {
         JpegRTPHeader var4 = (JpegRTPHeader)var1;
         var2 = var3;
         if (this.typeSpecific == var4.typeSpecific) {
            var2 = var3;
            if (this.fragmentOffset == var4.fragmentOffset) {
               var2 = var3;
               if (this.type == var4.type) {
                  var2 = var3;
                  if (this.field_232 == var4.field_232) {
                     var2 = var3;
                     if (this.width == var4.width) {
                        var2 = var3;
                        if (this.height == var4.height) {
                           var2 = true;
                        }
                     }
                  }
               }
            }
         }

         return var2;
      }
   }

   public int getFragmentOffset() {
      return this.fragmentOffset;
   }

   public int getHeightInBlocks() {
      return UnsignedUtils.uByteToInt(this.height);
   }

   public int getHeightInPixels() {
      return UnsignedUtils.uByteToInt(this.height) * 8;
   }

   public int getQ() {
      return UnsignedUtils.uByteToInt(this.field_232);
   }

   public int getType() {
      return UnsignedUtils.uByteToInt(this.type);
   }

   public int getTypeSpecific() {
      return UnsignedUtils.uByteToInt(this.typeSpecific);
   }

   public int getWidthInBlocks() {
      return UnsignedUtils.uByteToInt(this.width);
   }

   public int getWidthInPixels() {
      return UnsignedUtils.uByteToInt(this.width) * 8;
   }

   public int hashCode() {
      return this.typeSpecific + this.fragmentOffset + this.type + this.field_232 + this.width + this.height;
   }

   public byte[] toBytes() {
      byte[] var3 = new byte[8];
      int var1 = 0 + 1;
      var3[0] = this.typeSpecific;
      encode3ByteIntBE(this.fragmentOffset, var3, var1);
      int var2 = var1 + 3;
      var1 = var2 + 1;
      var3[var2] = this.type;
      var2 = var1 + 1;
      var3[var1] = this.field_232;
      var1 = var2 + 1;
      var3[var2] = this.width;
      var3[var1] = this.height;
      return var3;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("typeSpecific=");
      var1.append(this.getTypeSpecific());
      var1.append(" fragmentOffset=");
      var1.append(this.getFragmentOffset());
      var1.append(" type=");
      var1.append(this.getType());
      var1.append(" q=");
      var1.append(this.getQ());
      var1.append(" w=");
      var1.append(this.getWidthInPixels());
      var1.append(" h=");
      var1.append(this.getHeightInPixels());
      return var1.toString();
   }
}
