package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.widget.ImageView;

public class ImageViewCompat {
   private ImageViewCompat() {
   }

   public static ColorStateList getImageTintList(ImageView var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.getImageTintList();
      } else {
         return var0 instanceof TintableImageSourceView ? ((TintableImageSourceView)var0).getSupportImageTintList() : null;
      }
   }

   public static Mode getImageTintMode(ImageView var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.getImageTintMode();
      } else {
         return var0 instanceof TintableImageSourceView ? ((TintableImageSourceView)var0).getSupportImageTintMode() : null;
      }
   }

   public static void setImageTintList(ImageView var0, ColorStateList var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setImageTintList(var1);
         if (VERSION.SDK_INT == 21) {
            Drawable var2 = var0.getDrawable();
            if (var2 != null && var0.getImageTintList() != null) {
               if (var2.isStateful()) {
                  var2.setState(var0.getDrawableState());
               }

               var0.setImageDrawable(var2);
            }

            return;
         }
      } else if (var0 instanceof TintableImageSourceView) {
         ((TintableImageSourceView)var0).setSupportImageTintList(var1);
      }

   }

   public static void setImageTintMode(ImageView var0, Mode var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setImageTintMode(var1);
         if (VERSION.SDK_INT == 21) {
            Drawable var2 = var0.getDrawable();
            if (var2 != null && var0.getImageTintList() != null) {
               if (var2.isStateful()) {
                  var2.setState(var0.getDrawableState());
               }

               var0.setImageDrawable(var2);
            }

            return;
         }
      } else if (var0 instanceof TintableImageSourceView) {
         ((TintableImageSourceView)var0).setSupportImageTintMode(var1);
      }

   }
}
