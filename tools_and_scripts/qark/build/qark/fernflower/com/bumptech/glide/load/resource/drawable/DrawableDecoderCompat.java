package com.bumptech.glide.load.resource.drawable;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public final class DrawableDecoderCompat {
   private static volatile boolean shouldCallAppCompatResources = true;

   private DrawableDecoderCompat() {
   }

   public static Drawable getDrawable(Context var0, int var1, Theme var2) {
      return getDrawable(var0, var0, var1, var2);
   }

   public static Drawable getDrawable(Context var0, Context var1, int var2) {
      return getDrawable(var0, var1, var2, (Theme)null);
   }

   private static Drawable getDrawable(Context var0, Context var1, int var2, Theme var3) {
      try {
         if (shouldCallAppCompatResources) {
            Drawable var4 = loadDrawableV7(var1, var2, var3);
            return var4;
         }
      } catch (NoClassDefFoundError var5) {
         shouldCallAppCompatResources = false;
      } catch (IllegalStateException var6) {
         if (!var0.getPackageName().equals(var1.getPackageName())) {
            return ContextCompat.getDrawable(var1, var2);
         }

         throw var6;
      } catch (NotFoundException var7) {
      }

      if (var3 == null) {
         var3 = var1.getTheme();
      }

      return loadDrawableV4(var1, var2, var3);
   }

   private static Drawable loadDrawableV4(Context var0, int var1, Theme var2) {
      return ResourcesCompat.getDrawable(var0.getResources(), var1, var2);
   }

   private static Drawable loadDrawableV7(Context var0, int var1, Theme var2) {
      if (var2 != null) {
         var0 = new ContextThemeWrapper((Context)var0, var2);
      }

      return AppCompatResources.getDrawable((Context)var0, var1);
   }
}
