package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;

public class IconCompat {
   private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25F;
   private static final int AMBIENT_SHADOW_ALPHA = 30;
   private static final float BLUR_FACTOR = 0.010416667F;
   private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667F;
   private static final float ICON_DIAMETER_FACTOR = 0.9166667F;
   private static final int KEY_SHADOW_ALPHA = 61;
   private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334F;
   private static final int TYPE_ADAPTIVE_BITMAP = 5;
   private static final int TYPE_BITMAP = 1;
   private static final int TYPE_DATA = 3;
   private static final int TYPE_RESOURCE = 2;
   private static final int TYPE_URI = 4;
   private int mInt1;
   private int mInt2;
   private Object mObj1;
   private final int mType;

   private IconCompat(int var1) {
      this.mType = var1;
   }

   @VisibleForTesting
   static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap var0) {
      int var4 = (int)((float)Math.min(var0.getWidth(), var0.getHeight()) * 0.6666667F);
      Bitmap var5 = Bitmap.createBitmap(var4, var4, Config.ARGB_8888);
      Canvas var6 = new Canvas(var5);
      Paint var7 = new Paint(3);
      float var1 = (float)var4 * 0.5F;
      float var2 = 0.9166667F * var1;
      float var3 = (float)var4 * 0.010416667F;
      var7.setColor(0);
      var7.setShadowLayer(var3, 0.0F, (float)var4 * 0.020833334F, 1023410176);
      var6.drawCircle(var1, var1, var2, var7);
      var7.setShadowLayer(var3, 0.0F, 0.0F, 503316480);
      var6.drawCircle(var1, var1, var2, var7);
      var7.clearShadowLayer();
      var7.setColor(-16777216);
      BitmapShader var8 = new BitmapShader(var0, TileMode.CLAMP, TileMode.CLAMP);
      Matrix var9 = new Matrix();
      var9.setTranslate((float)(-(var0.getWidth() - var4) / 2), (float)(-(var0.getHeight() - var4) / 2));
      var8.setLocalMatrix(var9);
      var7.setShader(var8);
      var6.drawCircle(var1, var1, var2, var7);
      var6.setBitmap((Bitmap)null);
      return var5;
   }

   public static IconCompat createWithAdaptiveBitmap(Bitmap var0) {
      if (var0 != null) {
         IconCompat var1 = new IconCompat(5);
         var1.mObj1 = var0;
         return var1;
      } else {
         throw new IllegalArgumentException("Bitmap must not be null.");
      }
   }

   public static IconCompat createWithBitmap(Bitmap var0) {
      if (var0 != null) {
         IconCompat var1 = new IconCompat(1);
         var1.mObj1 = var0;
         return var1;
      } else {
         throw new IllegalArgumentException("Bitmap must not be null.");
      }
   }

   public static IconCompat createWithContentUri(Uri var0) {
      if (var0 != null) {
         return createWithContentUri(var0.toString());
      } else {
         throw new IllegalArgumentException("Uri must not be null.");
      }
   }

   public static IconCompat createWithContentUri(String var0) {
      if (var0 != null) {
         IconCompat var1 = new IconCompat(4);
         var1.mObj1 = var0;
         return var1;
      } else {
         throw new IllegalArgumentException("Uri must not be null.");
      }
   }

   public static IconCompat createWithData(byte[] var0, int var1, int var2) {
      if (var0 != null) {
         IconCompat var3 = new IconCompat(3);
         var3.mObj1 = var0;
         var3.mInt1 = var1;
         var3.mInt2 = var2;
         return var3;
      } else {
         throw new IllegalArgumentException("Data must not be null.");
      }
   }

   public static IconCompat createWithResource(Context var0, @DrawableRes int var1) {
      if (var0 != null) {
         IconCompat var2 = new IconCompat(2);
         var2.mInt1 = var1;
         var2.mObj1 = var0;
         return var2;
      } else {
         throw new IllegalArgumentException("Context must not be null.");
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void addToShortcutIntent(Intent var1) {
      int var2 = this.mType;
      if (var2 != 1) {
         if (var2 != 2) {
            if (var2 == 5) {
               var1.putExtra("android.intent.extra.shortcut.ICON", createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1));
            } else {
               throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
            }
         } else {
            var1.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext((Context)this.mObj1, this.mInt1));
         }
      } else {
         var1.putExtra("android.intent.extra.shortcut.ICON", (Bitmap)this.mObj1);
      }
   }

   @TargetApi(26)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public Icon toIcon() {
      int var1 = this.mType;
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 != 3) {
               if (var1 != 4) {
                  if (var1 == 5) {
                     return VERSION.SDK_INT >= 26 ? Icon.createWithAdaptiveBitmap((Bitmap)this.mObj1) : Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1));
                  } else {
                     throw new IllegalArgumentException("Unknown type");
                  }
               } else {
                  return Icon.createWithContentUri((String)this.mObj1);
               }
            } else {
               return Icon.createWithData((byte[])((byte[])this.mObj1), this.mInt1, this.mInt2);
            }
         } else {
            return Icon.createWithResource((Context)this.mObj1, this.mInt1);
         }
      } else {
         return Icon.createWithBitmap((Bitmap)this.mObj1);
      }
   }
}
