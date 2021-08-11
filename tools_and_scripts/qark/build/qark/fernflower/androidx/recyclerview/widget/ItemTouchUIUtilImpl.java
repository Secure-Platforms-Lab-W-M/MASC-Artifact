package androidx.recyclerview.widget;

import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.R.id;

class ItemTouchUIUtilImpl implements ItemTouchUIUtil {
   static final ItemTouchUIUtil INSTANCE = new ItemTouchUIUtilImpl();

   private static float findMaxElevation(RecyclerView var0, View var1) {
      int var6 = var0.getChildCount();
      float var2 = 0.0F;

      float var3;
      for(int var5 = 0; var5 < var6; var2 = var3) {
         View var7 = var0.getChildAt(var5);
         if (var7 == var1) {
            var3 = var2;
         } else {
            float var4 = ViewCompat.getElevation(var7);
            var3 = var2;
            if (var4 > var2) {
               var3 = var4;
            }
         }

         ++var5;
      }

      return var2;
   }

   public void clearView(View var1) {
      if (VERSION.SDK_INT >= 21) {
         Object var2 = var1.getTag(id.item_touch_helper_previous_elevation);
         if (var2 instanceof Float) {
            ViewCompat.setElevation(var1, (Float)var2);
         }

         var1.setTag(id.item_touch_helper_previous_elevation, (Object)null);
      }

      var1.setTranslationX(0.0F);
      var1.setTranslationY(0.0F);
   }

   public void onDraw(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7) {
      if (VERSION.SDK_INT >= 21 && var7 && var3.getTag(id.item_touch_helper_previous_elevation) == null) {
         float var8 = ViewCompat.getElevation(var3);
         ViewCompat.setElevation(var3, findMaxElevation(var2, var3) + 1.0F);
         var3.setTag(id.item_touch_helper_previous_elevation, var8);
      }

      var3.setTranslationX(var4);
      var3.setTranslationY(var5);
   }

   public void onDrawOver(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7) {
   }

   public void onSelected(View var1) {
   }
}
