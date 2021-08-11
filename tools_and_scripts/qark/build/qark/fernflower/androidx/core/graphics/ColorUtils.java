package androidx.core.graphics;

import android.graphics.Color;
import java.util.Objects;

public final class ColorUtils {
   private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
   private static final int MIN_ALPHA_SEARCH_PRECISION = 1;
   private static final ThreadLocal TEMP_ARRAY = new ThreadLocal();
   private static final double XYZ_EPSILON = 0.008856D;
   private static final double XYZ_KAPPA = 903.3D;
   private static final double XYZ_WHITE_REFERENCE_X = 95.047D;
   private static final double XYZ_WHITE_REFERENCE_Y = 100.0D;
   private static final double XYZ_WHITE_REFERENCE_Z = 108.883D;

   private ColorUtils() {
   }

   public static int HSLToColor(float[] var0) {
      float var1 = var0[0];
      float var2 = var0[1];
      float var3 = var0[2];
      var2 = (1.0F - Math.abs(var3 * 2.0F - 1.0F)) * var2;
      var3 -= 0.5F * var2;
      float var4 = (1.0F - Math.abs(var1 / 60.0F % 2.0F - 1.0F)) * var2;
      int var8 = (int)var1 / 60;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      switch(var8) {
      case 0:
         var5 = Math.round((var2 + var3) * 255.0F);
         var6 = Math.round((var4 + var3) * 255.0F);
         var7 = Math.round(255.0F * var3);
         break;
      case 1:
         var5 = Math.round((var4 + var3) * 255.0F);
         var6 = Math.round((var2 + var3) * 255.0F);
         var7 = Math.round(255.0F * var3);
         break;
      case 2:
         var5 = Math.round(var3 * 255.0F);
         var6 = Math.round((var2 + var3) * 255.0F);
         var7 = Math.round((var4 + var3) * 255.0F);
         break;
      case 3:
         var5 = Math.round(var3 * 255.0F);
         var6 = Math.round((var4 + var3) * 255.0F);
         var7 = Math.round((var2 + var3) * 255.0F);
         break;
      case 4:
         var5 = Math.round((var4 + var3) * 255.0F);
         var6 = Math.round(var3 * 255.0F);
         var7 = Math.round((var2 + var3) * 255.0F);
         break;
      case 5:
      case 6:
         var5 = Math.round((var2 + var3) * 255.0F);
         var6 = Math.round(var3 * 255.0F);
         var7 = Math.round((var4 + var3) * 255.0F);
      }

      return Color.rgb(constrain(var5, 0, 255), constrain(var6, 0, 255), constrain(var7, 0, 255));
   }

   public static int LABToColor(double var0, double var2, double var4) {
      double[] var6 = getTempDouble3Array();
      LABToXYZ(var0, var2, var4, var6);
      return XYZToColor(var6[0], var6[1], var6[2]);
   }

   public static void LABToXYZ(double var0, double var2, double var4, double[] var6) {
      double var9 = (var0 + 16.0D) / 116.0D;
      double var11 = var2 / 500.0D + var9;
      double var7 = var9 - var4 / 200.0D;
      var2 = Math.pow(var11, 3.0D);
      if (var2 <= 0.008856D) {
         var2 = (var11 * 116.0D - 16.0D) / 903.3D;
      }

      if (var0 > 7.9996247999999985D) {
         var0 = Math.pow(var9, 3.0D);
      } else {
         var0 /= 903.3D;
      }

      var4 = Math.pow(var7, 3.0D);
      if (var4 <= 0.008856D) {
         var4 = (116.0D * var7 - 16.0D) / 903.3D;
      }

      var6[0] = 95.047D * var2;
      var6[1] = 100.0D * var0;
      var6[2] = 108.883D * var4;
   }

   public static void RGBToHSL(int var0, int var1, int var2, float[] var3) {
      float var4 = (float)var0 / 255.0F;
      float var6 = (float)var1 / 255.0F;
      float var8 = (float)var2 / 255.0F;
      float var9 = Math.max(var4, Math.max(var6, var8));
      float var10 = Math.min(var4, Math.min(var6, var8));
      float var5 = var9 - var10;
      float var7 = (var9 + var10) / 2.0F;
      if (var9 == var10) {
         var4 = 0.0F;
         var5 = 0.0F;
      } else {
         if (var9 == var4) {
            var4 = (var6 - var8) / var5 % 6.0F;
         } else if (var9 == var6) {
            var4 = (var8 - var4) / var5 + 2.0F;
         } else {
            var4 = (var4 - var6) / var5 + 4.0F;
         }

         var6 = var5 / (1.0F - Math.abs(2.0F * var7 - 1.0F));
         var5 = var4;
         var4 = var6;
      }

      var6 = 60.0F * var5 % 360.0F;
      var5 = var6;
      if (var6 < 0.0F) {
         var5 = var6 + 360.0F;
      }

      var3[0] = constrain(var5, 0.0F, 360.0F);
      var3[1] = constrain(var4, 0.0F, 1.0F);
      var3[2] = constrain(var7, 0.0F, 1.0F);
   }

   public static void RGBToLAB(int var0, int var1, int var2, double[] var3) {
      RGBToXYZ(var0, var1, var2, var3);
      XYZToLAB(var3[0], var3[1], var3[2], var3);
   }

   public static void RGBToXYZ(int var0, int var1, int var2, double[] var3) {
      if (var3.length == 3) {
         double var4 = (double)var0 / 255.0D;
         if (var4 < 0.04045D) {
            var4 /= 12.92D;
         } else {
            var4 = Math.pow((var4 + 0.055D) / 1.055D, 2.4D);
         }

         double var6 = (double)var1 / 255.0D;
         if (var6 < 0.04045D) {
            var6 /= 12.92D;
         } else {
            var6 = Math.pow((var6 + 0.055D) / 1.055D, 2.4D);
         }

         double var8 = (double)var2 / 255.0D;
         if (var8 < 0.04045D) {
            var8 /= 12.92D;
         } else {
            var8 = Math.pow((0.055D + var8) / 1.055D, 2.4D);
         }

         var3[0] = (0.4124D * var4 + 0.3576D * var6 + 0.1805D * var8) * 100.0D;
         var3[1] = (0.2126D * var4 + 0.7152D * var6 + 0.0722D * var8) * 100.0D;
         var3[2] = (0.0193D * var4 + 0.1192D * var6 + 0.9505D * var8) * 100.0D;
      } else {
         throw new IllegalArgumentException("outXyz must have a length of 3.");
      }
   }

   public static int XYZToColor(double var0, double var2, double var4) {
      double var8 = (3.2406D * var0 + -1.5372D * var2 + -0.4986D * var4) / 100.0D;
      double var6 = (-0.9689D * var0 + 1.8758D * var2 + 0.0415D * var4) / 100.0D;
      var4 = (0.0557D * var0 + -0.204D * var2 + 1.057D * var4) / 100.0D;
      if (var8 > 0.0031308D) {
         var0 = Math.pow(var8, 0.4166666666666667D) * 1.055D - 0.055D;
      } else {
         var0 = var8 * 12.92D;
      }

      if (var6 > 0.0031308D) {
         var2 = Math.pow(var6, 0.4166666666666667D) * 1.055D - 0.055D;
      } else {
         var2 = var6 * 12.92D;
      }

      if (var4 > 0.0031308D) {
         var4 = Math.pow(var4, 0.4166666666666667D) * 1.055D - 0.055D;
      } else {
         var4 *= 12.92D;
      }

      return Color.rgb(constrain((int)Math.round(var0 * 255.0D), 0, 255), constrain((int)Math.round(var2 * 255.0D), 0, 255), constrain((int)Math.round(255.0D * var4), 0, 255));
   }

   public static void XYZToLAB(double var0, double var2, double var4, double[] var6) {
      if (var6.length == 3) {
         var0 = pivotXyzComponent(var0 / 95.047D);
         var2 = pivotXyzComponent(var2 / 100.0D);
         var4 = pivotXyzComponent(var4 / 108.883D);
         var6[0] = Math.max(0.0D, 116.0D * var2 - 16.0D);
         var6[1] = (var0 - var2) * 500.0D;
         var6[2] = (var2 - var4) * 200.0D;
      } else {
         throw new IllegalArgumentException("outLab must have a length of 3.");
      }
   }

   public static int blendARGB(int var0, int var1, float var2) {
      float var3 = 1.0F - var2;
      float var4 = (float)Color.alpha(var0);
      float var5 = (float)Color.alpha(var1);
      float var6 = (float)Color.red(var0);
      float var7 = (float)Color.red(var1);
      float var8 = (float)Color.green(var0);
      float var9 = (float)Color.green(var1);
      float var10 = (float)Color.blue(var0);
      float var11 = (float)Color.blue(var1);
      return Color.argb((int)(var4 * var3 + var5 * var2), (int)(var6 * var3 + var7 * var2), (int)(var8 * var3 + var9 * var2), (int)(var10 * var3 + var11 * var2));
   }

   public static void blendHSL(float[] var0, float[] var1, float var2, float[] var3) {
      if (var3.length == 3) {
         float var4 = 1.0F - var2;
         var3[0] = circularInterpolate(var0[0], var1[0], var2);
         var3[1] = var0[1] * var4 + var1[1] * var2;
         var3[2] = var0[2] * var4 + var1[2] * var2;
      } else {
         throw new IllegalArgumentException("result must have a length of 3.");
      }
   }

   public static void blendLAB(double[] var0, double[] var1, double var2, double[] var4) {
      if (var4.length == 3) {
         double var5 = 1.0D - var2;
         var4[0] = var0[0] * var5 + var1[0] * var2;
         var4[1] = var0[1] * var5 + var1[1] * var2;
         var4[2] = var0[2] * var5 + var1[2] * var2;
      } else {
         throw new IllegalArgumentException("outResult must have a length of 3.");
      }
   }

   public static double calculateContrast(int var0, int var1) {
      if (Color.alpha(var1) == 255) {
         int var6 = var0;
         if (Color.alpha(var0) < 255) {
            var6 = compositeColors(var0, var1);
         }

         double var2 = calculateLuminance(var6) + 0.05D;
         double var4 = calculateLuminance(var1) + 0.05D;
         return Math.max(var2, var4) / Math.min(var2, var4);
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("background can not be translucent: #");
         var7.append(Integer.toHexString(var1));
         throw new IllegalArgumentException(var7.toString());
      }
   }

   public static double calculateLuminance(int var0) {
      double[] var1 = getTempDouble3Array();
      colorToXYZ(var0, var1);
      return var1[1] / 100.0D;
   }

   public static int calculateMinimumAlpha(int var0, int var1, float var2) {
      if (Color.alpha(var1) != 255) {
         StringBuilder var7 = new StringBuilder();
         var7.append("background can not be translucent: #");
         var7.append(Integer.toHexString(var1));
         throw new IllegalArgumentException(var7.toString());
      } else if (calculateContrast(setAlphaComponent(var0, 255), var1) < (double)var2) {
         return -1;
      } else {
         int var3 = 0;
         int var5 = 0;

         int var4;
         for(var4 = 255; var3 <= 10 && var4 - var5 > 1; ++var3) {
            int var6 = (var5 + var4) / 2;
            if (calculateContrast(setAlphaComponent(var0, var6), var1) < (double)var2) {
               var5 = var6;
            } else {
               var4 = var6;
            }
         }

         return var4;
      }
   }

   static float circularInterpolate(float var0, float var1, float var2) {
      float var3 = var0;
      float var4 = var1;
      if (Math.abs(var1 - var0) > 180.0F) {
         if (var1 > var0) {
            var3 = var0 + 360.0F;
            var4 = var1;
         } else {
            var4 = var1 + 360.0F;
            var3 = var0;
         }
      }

      return ((var4 - var3) * var2 + var3) % 360.0F;
   }

   public static void colorToHSL(int var0, float[] var1) {
      RGBToHSL(Color.red(var0), Color.green(var0), Color.blue(var0), var1);
   }

   public static void colorToLAB(int var0, double[] var1) {
      RGBToLAB(Color.red(var0), Color.green(var0), Color.blue(var0), var1);
   }

   public static void colorToXYZ(int var0, double[] var1) {
      RGBToXYZ(Color.red(var0), Color.green(var0), Color.blue(var0), var1);
   }

   private static int compositeAlpha(int var0, int var1) {
      return 255 - (255 - var1) * (255 - var0) / 255;
   }

   public static int compositeColors(int var0, int var1) {
      int var2 = Color.alpha(var1);
      int var3 = Color.alpha(var0);
      int var4 = compositeAlpha(var3, var2);
      return Color.argb(var4, compositeComponent(Color.red(var0), var3, Color.red(var1), var2, var4), compositeComponent(Color.green(var0), var3, Color.green(var1), var2, var4), compositeComponent(Color.blue(var0), var3, Color.blue(var1), var2, var4));
   }

   public static Color compositeColors(Color var0, Color var1) {
      if (!Objects.equals(var0.getModel(), var1.getModel())) {
         StringBuilder var10 = new StringBuilder();
         var10.append("Color models must match (");
         var10.append(var0.getModel());
         var10.append(" vs. ");
         var10.append(var1.getModel());
         var10.append(")");
         throw new IllegalArgumentException(var10.toString());
      } else {
         if (!Objects.equals(var1.getColorSpace(), var0.getColorSpace())) {
            var0 = var0.convert(var1.getColorSpace());
         }

         float[] var8 = var0.getComponents();
         float[] var9 = var1.getComponents();
         float var5 = var0.alpha();
         float var4 = var1.alpha() * (1.0F - var5);
         int var7 = var1.getComponentCount() - 1;
         var9[var7] = var5 + var4;
         float var3 = var5;
         float var2 = var4;
         if (var9[var7] > 0.0F) {
            var3 = var5 / var9[var7];
            var2 = var4 / var9[var7];
         }

         for(int var6 = 0; var6 < var7; ++var6) {
            var9[var6] = var8[var6] * var3 + var9[var6] * var2;
         }

         return Color.valueOf(var9, var1.getColorSpace());
      }
   }

   private static int compositeComponent(int var0, int var1, int var2, int var3, int var4) {
      return var4 == 0 ? 0 : (var0 * 255 * var1 + var2 * var3 * (255 - var1)) / (var4 * 255);
   }

   private static float constrain(float var0, float var1, float var2) {
      if (var0 < var1) {
         return var1;
      } else {
         return var0 > var2 ? var2 : var0;
      }
   }

   private static int constrain(int var0, int var1, int var2) {
      if (var0 < var1) {
         return var1;
      } else {
         return var0 > var2 ? var2 : var0;
      }
   }

   public static double distanceEuclidean(double[] var0, double[] var1) {
      return Math.sqrt(Math.pow(var0[0] - var1[0], 2.0D) + Math.pow(var0[1] - var1[1], 2.0D) + Math.pow(var0[2] - var1[2], 2.0D));
   }

   private static double[] getTempDouble3Array() {
      double[] var1 = (double[])TEMP_ARRAY.get();
      double[] var0 = var1;
      if (var1 == null) {
         var0 = new double[3];
         TEMP_ARRAY.set(var0);
      }

      return var0;
   }

   private static double pivotXyzComponent(double var0) {
      return var0 > 0.008856D ? Math.pow(var0, 0.3333333333333333D) : (903.3D * var0 + 16.0D) / 116.0D;
   }

   public static int setAlphaComponent(int var0, int var1) {
      if (var1 >= 0 && var1 <= 255) {
         return 16777215 & var0 | var1 << 24;
      } else {
         throw new IllegalArgumentException("alpha must be between 0 and 255.");
      }
   }
}
