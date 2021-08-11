package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

public class ImageViewCompat {
   static final ImageViewCompat.ImageViewCompatImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 21) {
         IMPL = new ImageViewCompat.LollipopViewCompatImpl();
      } else {
         IMPL = new ImageViewCompat.BaseViewCompatImpl();
      }
   }

   private ImageViewCompat() {
   }

   public static ColorStateList getImageTintList(ImageView var0) {
      return IMPL.getImageTintList(var0);
   }

   public static Mode getImageTintMode(ImageView var0) {
      return IMPL.getImageTintMode(var0);
   }

   public static void setImageTintList(ImageView var0, ColorStateList var1) {
      IMPL.setImageTintList(var0, var1);
   }

   public static void setImageTintMode(ImageView var0, Mode var1) {
      IMPL.setImageTintMode(var0, var1);
   }

   static class BaseViewCompatImpl implements ImageViewCompat.ImageViewCompatImpl {
      public ColorStateList getImageTintList(ImageView var1) {
         return var1 instanceof TintableImageSourceView ? ((TintableImageSourceView)var1).getSupportImageTintList() : null;
      }

      public Mode getImageTintMode(ImageView var1) {
         return var1 instanceof TintableImageSourceView ? ((TintableImageSourceView)var1).getSupportImageTintMode() : null;
      }

      public void setImageTintList(ImageView var1, ColorStateList var2) {
         if (var1 instanceof TintableImageSourceView) {
            ((TintableImageSourceView)var1).setSupportImageTintList(var2);
         }

      }

      public void setImageTintMode(ImageView var1, Mode var2) {
         if (var1 instanceof TintableImageSourceView) {
            ((TintableImageSourceView)var1).setSupportImageTintMode(var2);
         }

      }
   }

   interface ImageViewCompatImpl {
      ColorStateList getImageTintList(ImageView var1);

      Mode getImageTintMode(ImageView var1);

      void setImageTintList(ImageView var1, ColorStateList var2);

      void setImageTintMode(ImageView var1, Mode var2);
   }

   @RequiresApi(21)
   static class LollipopViewCompatImpl extends ImageViewCompat.BaseViewCompatImpl {
      public ColorStateList getImageTintList(ImageView var1) {
         return var1.getImageTintList();
      }

      public Mode getImageTintMode(ImageView var1) {
         return var1.getImageTintMode();
      }

      public void setImageTintList(ImageView var1, ColorStateList var2) {
         var1.setImageTintList(var2);
         if (VERSION.SDK_INT == 21) {
            Drawable var4 = var1.getDrawable();
            boolean var3;
            if (var1.getImageTintList() != null && var1.getImageTintMode() != null) {
               var3 = true;
            } else {
               var3 = false;
            }

            if (var4 != null && var3) {
               if (var4.isStateful()) {
                  var4.setState(var1.getDrawableState());
               }

               var1.setImageDrawable(var4);
            }
         }

      }

      public void setImageTintMode(ImageView var1, Mode var2) {
         var1.setImageTintMode(var2);
         if (VERSION.SDK_INT == 21) {
            Drawable var4 = var1.getDrawable();
            boolean var3;
            if (var1.getImageTintList() != null && var1.getImageTintMode() != null) {
               var3 = true;
            } else {
               var3 = false;
            }

            if (var4 != null && var3) {
               if (var4.isStateful()) {
                  var4.setState(var1.getDrawableState());
               }

               var1.setImageDrawable(var4);
            }
         }

      }
   }
}
