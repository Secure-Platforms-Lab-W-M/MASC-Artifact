package net.sf.fmj.media.codec.video.jpeg;

import java.io.PrintStream;
import net.sf.fmj.utility.ArrayUtility;

public class JpegStripper {
   private static boolean STRIP = false;

   static void dump(int[] var0) {
      dump(var0, 10);
   }

   static void dump(int[] var0, int var1) {
      for(int var2 = 0; var2 < var0.length / var1 + 1; ++var2) {
         for(int var3 = var2 * var1; var3 < var2 * var1 + var1 && var3 < var0.length; ++var3) {
            String var5 = Integer.toHexString(var0[var3]);
            String var4 = var5;
            if (var5.length() < 2) {
               StringBuilder var7 = new StringBuilder();
               var7.append(0);
               var7.append(var5);
               var4 = var7.toString();
            }

            PrintStream var9 = System.out;
            StringBuilder var6 = new StringBuilder();
            var6.append(var4);
            var6.append(" ");
            var9.print(var6.toString());
         }

         System.out.println("");
      }

      PrintStream var8 = System.out;
      StringBuilder var10 = new StringBuilder();
      var10.append("Length: ");
      var10.append(var0.length);
      var8.println(var10.toString());
   }

   private static int findNextMarker(int[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         if (var0[var1] == 255 && var0[var1 + 1] != 0 && (var0[var1 + 1] < 208 || var0[var1 + 1] > 215)) {
            STRIP = true;
            return var1;
         }
      }

      STRIP = false;
      return var0.length;
   }

   public static byte[] removeHeaders(byte[] var0) {
      int[] var2 = new int[var0.length];

      int var1;
      for(var1 = 0; var1 < var0.length; ++var1) {
         var2[var1] = var0[var1] & 255;
      }

      int[] var3 = removeHeaders(var2);
      byte[] var4 = new byte[var3.length];

      for(var1 = 0; var1 < var3.length; ++var1) {
         var4[var1] = (byte)var3[var1];
      }

      return var4;
   }

   public static int[] removeHeaders(int[] var0) {
      return stripTrailingHeaders(stripLeadingHeaders(var0));
   }

   private static int[] stripHeader(int[] var0) {
      return ArrayUtility.copyOfRange((int[])var0, 2, var0.length);
   }

   private static int[] stripHeaderContent(int[] var0) {
      var0 = stripHeader(var0);
      return ArrayUtility.copyOfRange(var0, var0[0] * 256 + var0[1], var0.length);
   }

   private static int[] stripLeadingHeaders(int[] var0) {
      int[] var3 = var0;
      int[] var2 = var0;
      if (var0[0] == 255) {
         int var1 = var0[1];
         if (var1 == 192 || var1 == 196 || var1 == 221 || var1 == 224 || var1 == 218 || var1 == 219) {
            var3 = stripLeadingHeaders(stripHeaderContent(var0));
         }

         var2 = var3;
         if (var0[1] == 216) {
            var2 = stripLeadingHeaders(stripHeader(var3));
         }
      }

      return var2;
   }

   private static int[] stripOtherMarkers(int[] var0) {
      return ArrayUtility.copyOfRange((int[])var0, 0, findNextMarker(var0));
   }

   private static int[] stripTrailingHeaders(int[] var0) {
      int[] var1 = stripOtherMarkers(var0);
      var0 = var1;
      if (STRIP) {
         var0 = stripTrailingHeaders(var1);
      }

      return var0;
   }
}
