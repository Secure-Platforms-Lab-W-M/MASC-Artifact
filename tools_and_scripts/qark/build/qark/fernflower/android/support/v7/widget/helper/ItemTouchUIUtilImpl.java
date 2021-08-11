package android.support.v7.widget.helper;

import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.R$id;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class ItemTouchUIUtilImpl {
   static class Api21Impl extends ItemTouchUIUtilImpl.BaseImpl {
      private float findMaxElevation(RecyclerView var1, View var2) {
         int var6 = var1.getChildCount();
         float var3 = 0.0F;

         for(int var5 = 0; var5 < var6; ++var5) {
            View var7 = var1.getChildAt(var5);
            if (var7 != var2) {
               float var4 = ViewCompat.getElevation(var7);
               if (var4 > var3) {
                  var3 = var4;
               }
            }
         }

         return var3;
      }

      public void clearView(View var1) {
         Object var2 = var1.getTag(R$id.item_touch_helper_previous_elevation);
         if (var2 != null && var2 instanceof Float) {
            ViewCompat.setElevation(var1, (Float)var2);
         }

         var1.setTag(R$id.item_touch_helper_previous_elevation, (Object)null);
         super.clearView(var1);
      }

      public void onDraw(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7) {
         if (var7 && var3.getTag(R$id.item_touch_helper_previous_elevation) == null) {
            float var8 = ViewCompat.getElevation(var3);
            ViewCompat.setElevation(var3, this.findMaxElevation(var2, var3) + 1.0F);
            var3.setTag(R$id.item_touch_helper_previous_elevation, var8);
         }

         super.onDraw(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   static class BaseImpl implements ItemTouchUIUtil {
      public void clearView(View var1) {
         var1.setTranslationX(0.0F);
         var1.setTranslationY(0.0F);
      }

      public void onDraw(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7) {
         var3.setTranslationX(var4);
         var3.setTranslationY(var5);
      }

      public void onDrawOver(Canvas var1, RecyclerView var2, View var3, float var4, float var5, int var6, boolean var7) {
      }

      public void onSelected(View var1) {
      }
   }
}
