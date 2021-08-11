package com.bumptech.glide.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Looper;
import android.os.Build.VERSION;
import com.bumptech.glide.load.model.Model;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public final class Util {
   private static final int HASH_ACCUMULATOR = 17;
   private static final int HASH_MULTIPLIER = 31;
   private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
   private static final char[] SHA_256_CHARS = new char[64];

   private Util() {
   }

   public static void assertBackgroundThread() {
      if (!isOnBackgroundThread()) {
         throw new IllegalArgumentException("You must call this method on a background thread");
      }
   }

   public static void assertMainThread() {
      if (!isOnMainThread()) {
         throw new IllegalArgumentException("You must call this method on the main thread");
      }
   }

   public static boolean bothModelsNullEquivalentOrEquals(Object var0, Object var1) {
      if (var0 == null) {
         return var1 == null;
      } else {
         return var0 instanceof Model ? ((Model)var0).isEquivalentTo(var1) : var0.equals(var1);
      }
   }

   public static boolean bothNullOrEqual(Object var0, Object var1) {
      if (var0 == null) {
         return var1 == null;
      } else {
         return var0.equals(var1);
      }
   }

   private static String bytesToHex(byte[] var0, char[] var1) {
      for(int var2 = 0; var2 < var0.length; ++var2) {
         int var3 = var0[var2] & 255;
         char[] var4 = HEX_CHAR_ARRAY;
         var1[var2 * 2] = var4[var3 >>> 4];
         var1[var2 * 2 + 1] = var4[var3 & 15];
      }

      return new String(var1);
   }

   public static Queue createQueue(int var0) {
      return new ArrayDeque(var0);
   }

   public static int getBitmapByteSize(int var0, int var1, Config var2) {
      return var0 * var1 * getBytesPerPixel(var2);
   }

   public static int getBitmapByteSize(Bitmap var0) {
      if (var0.isRecycled()) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Cannot obtain size for recycled Bitmap: ");
         var2.append(var0);
         var2.append("[");
         var2.append(var0.getWidth());
         var2.append("x");
         var2.append(var0.getHeight());
         var2.append("] ");
         var2.append(var0.getConfig());
         throw new IllegalStateException(var2.toString());
      } else {
         if (VERSION.SDK_INT >= 19) {
            try {
               int var1 = var0.getAllocationByteCount();
               return var1;
            } catch (NullPointerException var3) {
            }
         }

         return var0.getHeight() * var0.getRowBytes();
      }
   }

   private static int getBytesPerPixel(Config var0) {
      Config var2 = var0;
      if (var0 == null) {
         var2 = Config.ARGB_8888;
      }

      int var1 = null.$SwitchMap$android$graphics$Bitmap$Config[var2.ordinal()];
      if (var1 != 1) {
         if (var1 != 2 && var1 != 3) {
            return var1 != 4 ? 4 : 8;
         } else {
            return 2;
         }
      } else {
         return 1;
      }
   }

   @Deprecated
   public static int getSize(Bitmap var0) {
      return getBitmapByteSize(var0);
   }

   public static List getSnapshot(Collection var0) {
      ArrayList var1 = new ArrayList(var0.size());
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         Object var2 = var3.next();
         if (var2 != null) {
            var1.add(var2);
         }
      }

      return var1;
   }

   public static int hashCode(float var0) {
      return hashCode(var0, 17);
   }

   public static int hashCode(float var0, int var1) {
      return hashCode(Float.floatToIntBits(var0), var1);
   }

   public static int hashCode(int var0) {
      return hashCode(var0, 17);
   }

   public static int hashCode(int var0, int var1) {
      return var1 * 31 + var0;
   }

   public static int hashCode(Object var0, int var1) {
      int var2;
      if (var0 == null) {
         var2 = 0;
      } else {
         var2 = var0.hashCode();
      }

      return hashCode(var2, var1);
   }

   public static int hashCode(boolean var0) {
      return hashCode(var0, 17);
   }

   public static int hashCode(boolean var0, int var1) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public static boolean isOnBackgroundThread() {
      return isOnMainThread() ^ true;
   }

   public static boolean isOnMainThread() {
      return Looper.myLooper() == Looper.getMainLooper();
   }

   private static boolean isValidDimension(int var0) {
      return var0 > 0 || var0 == Integer.MIN_VALUE;
   }

   public static boolean isValidDimensions(int var0, int var1) {
      return isValidDimension(var0) && isValidDimension(var1);
   }

   public static String sha256BytesToHex(byte[] param0) {
      // $FF: Couldn't be decompiled
   }
}
