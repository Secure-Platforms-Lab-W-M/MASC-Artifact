package androidx.appcompat.app;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

class ActionBarDrawerToggleHoneycomb {
   private static final String TAG = "ActionBarDrawerToggleHC";
   private static final int[] THEME_ATTRS = new int[]{16843531};

   private ActionBarDrawerToggleHoneycomb() {
   }

   public static Drawable getThemeUpIndicator(Activity var0) {
      TypedArray var2 = var0.obtainStyledAttributes(THEME_ATTRS);
      Drawable var1 = var2.getDrawable(0);
      var2.recycle();
      return var1;
   }

   public static ActionBarDrawerToggleHoneycomb.SetIndicatorInfo setActionBarDescription(ActionBarDrawerToggleHoneycomb.SetIndicatorInfo var0, Activity var1, int var2) {
      ActionBarDrawerToggleHoneycomb.SetIndicatorInfo var3 = var0;
      if (var0 == null) {
         var3 = new ActionBarDrawerToggleHoneycomb.SetIndicatorInfo(var1);
      }

      if (var3.setHomeAsUpIndicator != null) {
         try {
            android.app.ActionBar var5 = var1.getActionBar();
            var3.setHomeActionContentDescription.invoke(var5, var2);
            if (VERSION.SDK_INT <= 19) {
               var5.setSubtitle(var5.getSubtitle());
            }

            return var3;
         } catch (Exception var4) {
            Log.w("ActionBarDrawerToggleHC", "Couldn't set content description via JB-MR2 API", var4);
         }
      }

      return var3;
   }

   public static ActionBarDrawerToggleHoneycomb.SetIndicatorInfo setActionBarUpIndicator(Activity var0, Drawable var1, int var2) {
      ActionBarDrawerToggleHoneycomb.SetIndicatorInfo var3 = new ActionBarDrawerToggleHoneycomb.SetIndicatorInfo(var0);
      if (var3.setHomeAsUpIndicator != null) {
         try {
            android.app.ActionBar var5 = var0.getActionBar();
            var3.setHomeAsUpIndicator.invoke(var5, var1);
            var3.setHomeActionContentDescription.invoke(var5, var2);
            return var3;
         } catch (Exception var4) {
            Log.w("ActionBarDrawerToggleHC", "Couldn't set home-as-up indicator via JB-MR2 API", var4);
            return var3;
         }
      } else if (var3.upIndicatorView != null) {
         var3.upIndicatorView.setImageDrawable(var1);
         return var3;
      } else {
         Log.w("ActionBarDrawerToggleHC", "Couldn't set home-as-up indicator");
         return var3;
      }
   }

   static class SetIndicatorInfo {
      public Method setHomeActionContentDescription;
      public Method setHomeAsUpIndicator;
      public ImageView upIndicatorView;

      SetIndicatorInfo(Activity var1) {
         try {
            this.setHomeAsUpIndicator = android.app.ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", Drawable.class);
            this.setHomeActionContentDescription = android.app.ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", Integer.TYPE);
         } catch (NoSuchMethodException var3) {
            View var4 = var1.findViewById(16908332);
            if (var4 != null) {
               ViewGroup var2 = (ViewGroup)var4.getParent();
               if (var2.getChildCount() == 2) {
                  var4 = var2.getChildAt(0);
                  View var5 = var2.getChildAt(1);
                  if (var4.getId() == 16908332) {
                     var4 = var5;
                  }

                  if (var4 instanceof ImageView) {
                     this.upIndicatorView = (ImageView)var4;
                  }

               }
            }
         }
      }
   }
}
