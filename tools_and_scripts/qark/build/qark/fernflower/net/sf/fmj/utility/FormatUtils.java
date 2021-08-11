package net.sf.fmj.utility;

import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import org.atalk.android.util.java.awt.Dimension;

public class FormatUtils {
   public static final Class audioFormatClass = AudioFormat.class;
   public static final Class byteArray = byte[].class;
   public static final Class formatArray = Format[].class;
   public static final Class intArray = int[].class;
   public static final Class shortArray = short[].class;
   public static final Class videoFormatClass = VideoFormat.class;

   public static boolean byteArraysEqual(byte[] var0, byte[] var1) {
      if (var0 == null && var1 == null) {
         return true;
      } else if (var0 != null) {
         if (var1 == null) {
            return false;
         } else if (var0.length != var1.length) {
            return false;
         } else {
            for(int var2 = 0; var2 < var0.length; ++var2) {
               if (var0[var2] != var1[var2]) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private static int charEncodingCodeVal(char var0) {
      if (var0 <= '_') {
         return var0 - 32;
      } else if (var0 == '`') {
         return -1;
      } else if (var0 <= 'z') {
         return var0 - 64;
      } else if (var0 <= 127) {
         return -1;
      } else if (var0 <= 191) {
         return -94;
      } else {
         return var0 <= 255 ? -93 : -1;
      }
   }

   public static Dimension clone(Dimension var0) {
      return var0 == null ? null : new Dimension(var0);
   }

   public static String frameRateToString(float var0) {
      var0 = (float)((long)(var0 * 10.0F)) / 10.0F;
      StringBuilder var1 = new StringBuilder();
      var1.append("");
      var1.append(var0);
      return var1.toString();
   }

   public static boolean isOneAssignableFromTheOther(Class var0, Class var1) {
      return var0 == var1 || var1.isAssignableFrom(var0) || var0.isAssignableFrom(var1);
   }

   public static boolean isSubclass(Class var0, Class var1) {
      if (var0 == var1) {
         return false;
      } else {
         return var1.isAssignableFrom(var0);
      }
   }

   public static boolean matches(double var0, double var2) {
      if (var0 != -1.0D) {
         if (var2 == -1.0D) {
            return true;
         } else {
            return var0 == var2;
         }
      } else {
         return true;
      }
   }

   public static boolean matches(float var0, float var1) {
      if (var0 != -1.0F) {
         if (var1 == -1.0F) {
            return true;
         } else {
            return var0 == var1;
         }
      } else {
         return true;
      }
   }

   public static boolean matches(int var0, int var1) {
      if (var0 != -1) {
         if (var1 == -1) {
            return true;
         } else {
            return var0 == var1;
         }
      } else {
         return true;
      }
   }

   public static boolean matches(Object var0, Object var1) {
      return var0 != null && var1 != null ? var0.equals(var1) : true;
   }

   public static boolean nullSafeEquals(Object var0, Object var1) {
      if (var0 == null && var1 == null) {
         return true;
      } else {
         return var0 != null && var1 != null ? var0.equals(var1) : false;
      }
   }

   public static boolean nullSafeEqualsIgnoreCase(String var0, String var1) {
      if (var0 == null && var1 == null) {
         return true;
      } else {
         return var0 != null && var1 != null ? var0.equalsIgnoreCase(var1) : false;
      }
   }

   public static boolean specified(double var0) {
      return var0 != -1.0D;
   }

   public static boolean specified(float var0) {
      return var0 != -1.0F;
   }

   public static boolean specified(int var0) {
      return var0 != -1;
   }

   public static boolean specified(Object var0) {
      return var0 != null;
   }

   public static long stringEncodingCodeVal(String var0) {
      long var2 = 0L;

      for(int var1 = 0; var1 < var0.length(); ++var1) {
         var2 = var2 * 64L + (long)charEncodingCodeVal(var0.charAt(var1));
      }

      return var2;
   }
}
