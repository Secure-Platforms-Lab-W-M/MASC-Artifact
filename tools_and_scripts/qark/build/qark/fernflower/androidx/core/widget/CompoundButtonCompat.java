package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Log;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

public final class CompoundButtonCompat {
   private static final String TAG = "CompoundButtonCompat";
   private static Field sButtonDrawableField;
   private static boolean sButtonDrawableFieldFetched;

   private CompoundButtonCompat() {
   }

   public static Drawable getButtonDrawable(CompoundButton var0) {
      if (VERSION.SDK_INT >= 23) {
         return var0.getButtonDrawable();
      } else {
         Field var1;
         if (!sButtonDrawableFieldFetched) {
            try {
               var1 = CompoundButton.class.getDeclaredField("mButtonDrawable");
               sButtonDrawableField = var1;
               var1.setAccessible(true);
            } catch (NoSuchFieldException var2) {
               Log.i("CompoundButtonCompat", "Failed to retrieve mButtonDrawable field", var2);
            }

            sButtonDrawableFieldFetched = true;
         }

         var1 = sButtonDrawableField;
         if (var1 != null) {
            try {
               Drawable var4 = (Drawable)var1.get(var0);
               return var4;
            } catch (IllegalAccessException var3) {
               Log.i("CompoundButtonCompat", "Failed to get button drawable via reflection", var3);
               sButtonDrawableField = null;
            }
         }

         return null;
      }
   }

   public static ColorStateList getButtonTintList(CompoundButton var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.getButtonTintList();
      } else {
         return var0 instanceof TintableCompoundButton ? ((TintableCompoundButton)var0).getSupportButtonTintList() : null;
      }
   }

   public static Mode getButtonTintMode(CompoundButton var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.getButtonTintMode();
      } else {
         return var0 instanceof TintableCompoundButton ? ((TintableCompoundButton)var0).getSupportButtonTintMode() : null;
      }
   }

   public static void setButtonTintList(CompoundButton var0, ColorStateList var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setButtonTintList(var1);
      } else {
         if (var0 instanceof TintableCompoundButton) {
            ((TintableCompoundButton)var0).setSupportButtonTintList(var1);
         }

      }
   }

   public static void setButtonTintMode(CompoundButton var0, Mode var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setButtonTintMode(var1);
      } else {
         if (var0 instanceof TintableCompoundButton) {
            ((TintableCompoundButton)var0).setSupportButtonTintMode(var1);
         }

      }
   }
}
