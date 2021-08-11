package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

@TargetApi(18)
@RequiresApi(18)
class ActionBarDrawerToggleJellybeanMR2 {
   private static final String TAG = "ActionBarDrawerToggleImplJellybeanMR2";
   private static final int[] THEME_ATTRS = new int[]{16843531};

   public static Drawable getThemeUpIndicator(Activity var0) {
      ActionBar var1 = ((Activity)var0).getActionBar();
      if (var1 != null) {
         var0 = var1.getThemedContext();
      }

      TypedArray var2 = ((Context)var0).obtainStyledAttributes((AttributeSet)null, THEME_ATTRS, 16843470, 0);
      Drawable var3 = var2.getDrawable(0);
      var2.recycle();
      return var3;
   }

   public static Object setActionBarDescription(Object var0, Activity var1, int var2) {
      ActionBar var3 = var1.getActionBar();
      if (var3 != null) {
         var3.setHomeActionContentDescription(var2);
      }

      return var0;
   }

   public static Object setActionBarUpIndicator(Object var0, Activity var1, Drawable var2, int var3) {
      ActionBar var4 = var1.getActionBar();
      if (var4 != null) {
         var4.setHomeAsUpIndicator(var2);
         var4.setHomeActionContentDescription(var3);
      }

      return var0;
   }
}
