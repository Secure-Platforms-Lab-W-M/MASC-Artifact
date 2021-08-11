package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

@TargetApi(11)
@RequiresApi(11)
class ActionBarDrawerToggleHoneycomb {
   private static final String TAG = "ActionBarDrawerToggleHoneycomb";
   private static final int[] THEME_ATTRS = new int[]{16843531};

   public static Drawable getThemeUpIndicator(Activity var0) {
      TypedArray var2 = var0.obtainStyledAttributes(THEME_ATTRS);
      Drawable var1 = var2.getDrawable(0);
      var2.recycle();
      return var1;
   }

   public static Object setActionBarDescription(Object var0, Activity var1, int var2) {
      Object var3 = var0;
      if (var0 == null) {
         var3 = new ActionBarDrawerToggleHoneycomb.SetIndicatorInfo(var1);
      }

      ActionBarDrawerToggleHoneycomb.SetIndicatorInfo var5 = (ActionBarDrawerToggleHoneycomb.SetIndicatorInfo)var3;
      if (var5.setHomeAsUpIndicator != null) {
         try {
            ActionBar var6 = var1.getActionBar();
            var5.setHomeActionContentDescription.invoke(var6, var2);
            if (VERSION.SDK_INT <= 19) {
               var6.setSubtitle(var6.getSubtitle());
            }
         } catch (Exception var4) {
            Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set content description via JB-MR2 API", var4);
            return var3;
         }
      }

      return var3;
   }

   public static Object setActionBarUpIndicator(Object var0, Activity var1, Drawable var2, int var3) {
      Object var4 = var0;
      if (var0 == null) {
         var4 = new ActionBarDrawerToggleHoneycomb.SetIndicatorInfo(var1);
      }

      ActionBarDrawerToggleHoneycomb.SetIndicatorInfo var6 = (ActionBarDrawerToggleHoneycomb.SetIndicatorInfo)var4;
      if (var6.setHomeAsUpIndicator != null) {
         try {
            ActionBar var7 = var1.getActionBar();
            var6.setHomeAsUpIndicator.invoke(var7, var2);
            var6.setHomeActionContentDescription.invoke(var7, var3);
            return var4;
         } catch (Exception var5) {
            Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator via JB-MR2 API", var5);
            return var4;
         }
      } else if (var6.upIndicatorView != null) {
         var6.upIndicatorView.setImageDrawable(var2);
         return var4;
      } else {
         Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator");
         return var4;
      }
   }

   private static class SetIndicatorInfo {
      public Method setHomeActionContentDescription;
      public Method setHomeAsUpIndicator;
      public ImageView upIndicatorView;

      SetIndicatorInfo(Activity var1) {
         try {
            this.setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", Drawable.class);
            this.setHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", Integer.TYPE);
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
                     return;
                  }
               }
            }
         }

      }
   }
}
