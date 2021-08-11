package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

public final class CompoundButtonCompat {
   private static final CompoundButtonCompat.CompoundButtonCompatBaseImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 23) {
         IMPL = new CompoundButtonCompat.CompoundButtonCompatApi23Impl();
      } else if (VERSION.SDK_INT >= 21) {
         IMPL = new CompoundButtonCompat.CompoundButtonCompatApi21Impl();
      } else {
         IMPL = new CompoundButtonCompat.CompoundButtonCompatBaseImpl();
      }
   }

   private CompoundButtonCompat() {
   }

   @Nullable
   public static Drawable getButtonDrawable(@NonNull CompoundButton var0) {
      return IMPL.getButtonDrawable(var0);
   }

   @Nullable
   public static ColorStateList getButtonTintList(@NonNull CompoundButton var0) {
      return IMPL.getButtonTintList(var0);
   }

   @Nullable
   public static Mode getButtonTintMode(@NonNull CompoundButton var0) {
      return IMPL.getButtonTintMode(var0);
   }

   public static void setButtonTintList(@NonNull CompoundButton var0, @Nullable ColorStateList var1) {
      IMPL.setButtonTintList(var0, var1);
   }

   public static void setButtonTintMode(@NonNull CompoundButton var0, @Nullable Mode var1) {
      IMPL.setButtonTintMode(var0, var1);
   }

   @RequiresApi(21)
   static class CompoundButtonCompatApi21Impl extends CompoundButtonCompat.CompoundButtonCompatBaseImpl {
      public ColorStateList getButtonTintList(CompoundButton var1) {
         return var1.getButtonTintList();
      }

      public Mode getButtonTintMode(CompoundButton var1) {
         return var1.getButtonTintMode();
      }

      public void setButtonTintList(CompoundButton var1, ColorStateList var2) {
         var1.setButtonTintList(var2);
      }

      public void setButtonTintMode(CompoundButton var1, Mode var2) {
         var1.setButtonTintMode(var2);
      }
   }

   @RequiresApi(23)
   static class CompoundButtonCompatApi23Impl extends CompoundButtonCompat.CompoundButtonCompatApi21Impl {
      public Drawable getButtonDrawable(CompoundButton var1) {
         return var1.getButtonDrawable();
      }
   }

   static class CompoundButtonCompatBaseImpl {
      private static final String TAG = "CompoundButtonCompat";
      private static Field sButtonDrawableField;
      private static boolean sButtonDrawableFieldFetched;

      public Drawable getButtonDrawable(CompoundButton var1) {
         if (!sButtonDrawableFieldFetched) {
            try {
               sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable");
               sButtonDrawableField.setAccessible(true);
            } catch (NoSuchFieldException var3) {
               Log.i("CompoundButtonCompat", "Failed to retrieve mButtonDrawable field", var3);
            }

            sButtonDrawableFieldFetched = true;
         }

         Field var2 = sButtonDrawableField;
         if (var2 != null) {
            try {
               Drawable var5 = (Drawable)var2.get(var1);
               return var5;
            } catch (IllegalAccessException var4) {
               Log.i("CompoundButtonCompat", "Failed to get button drawable via reflection", var4);
               sButtonDrawableField = null;
            }
         }

         return null;
      }

      public ColorStateList getButtonTintList(CompoundButton var1) {
         return var1 instanceof TintableCompoundButton ? ((TintableCompoundButton)var1).getSupportButtonTintList() : null;
      }

      public Mode getButtonTintMode(CompoundButton var1) {
         return var1 instanceof TintableCompoundButton ? ((TintableCompoundButton)var1).getSupportButtonTintMode() : null;
      }

      public void setButtonTintList(CompoundButton var1, ColorStateList var2) {
         if (var1 instanceof TintableCompoundButton) {
            ((TintableCompoundButton)var1).setSupportButtonTintList(var2);
         }

      }

      public void setButtonTintMode(CompoundButton var1, Mode var2) {
         if (var1 instanceof TintableCompoundButton) {
            ((TintableCompoundButton)var1).setSupportButtonTintMode(var2);
         }

      }
   }
}
