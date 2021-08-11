package androidx.viewpager2.widget;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

final class AnimateLayoutChangeDetector {
   private static final MarginLayoutParams ZERO_MARGIN_LAYOUT_PARAMS;
   private LinearLayoutManager mLayoutManager;

   static {
      MarginLayoutParams var0 = new MarginLayoutParams(-1, -1);
      ZERO_MARGIN_LAYOUT_PARAMS = var0;
      var0.setMargins(0, 0, 0, 0);
   }

   AnimateLayoutChangeDetector(LinearLayoutManager var1) {
      this.mLayoutManager = var1;
   }

   private boolean arePagesLaidOutContiguously() {
      int var4 = this.mLayoutManager.getChildCount();
      if (var4 == 0) {
         return true;
      } else {
         boolean var1;
         if (this.mLayoutManager.getOrientation() == 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         int[][] var6 = (int[][])Array.newInstance(Integer.TYPE, new int[]{var4, 2});

         int var2;
         for(var2 = 0; var2 < var4; ++var2) {
            View var7 = this.mLayoutManager.getChildAt(var2);
            if (var7 == null) {
               throw new IllegalStateException("null view contained in the view hierarchy");
            }

            LayoutParams var5 = var7.getLayoutParams();
            MarginLayoutParams var10;
            if (var5 instanceof MarginLayoutParams) {
               var10 = (MarginLayoutParams)var5;
            } else {
               var10 = ZERO_MARGIN_LAYOUT_PARAMS;
            }

            int[] var8 = var6[var2];
            int var3;
            if (var1) {
               var3 = var7.getLeft() - var10.leftMargin;
            } else {
               var3 = var7.getTop() - var10.topMargin;
            }

            var8[0] = var3;
            var8 = var6[var2];
            if (var1) {
               var3 = var7.getRight() + var10.rightMargin;
            } else {
               var3 = var7.getBottom() + var10.bottomMargin;
            }

            var8[1] = var3;
         }

         Arrays.sort(var6, new Comparator() {
            public int compare(int[] var1, int[] var2) {
               return var1[0] - var2[0];
            }
         });

         int var9;
         for(var9 = 1; var9 < var4; ++var9) {
            if (var6[var9 - 1][1] != var6[var9][0]) {
               return false;
            }
         }

         var9 = var6[0][1];
         var2 = var6[0][0];
         if (var6[0][0] <= 0) {
            if (var6[var4 - 1][1] < var9 - var2) {
               return false;
            } else {
               return true;
            }
         } else {
            return false;
         }
      }
   }

   private boolean hasRunningChangingLayoutTransition() {
      int var2 = this.mLayoutManager.getChildCount();

      for(int var1 = 0; var1 < var2; ++var1) {
         if (hasRunningChangingLayoutTransition(this.mLayoutManager.getChildAt(var1))) {
            return true;
         }
      }

      return false;
   }

   private static boolean hasRunningChangingLayoutTransition(View var0) {
      if (var0 instanceof ViewGroup) {
         ViewGroup var4 = (ViewGroup)var0;
         LayoutTransition var3 = var4.getLayoutTransition();
         if (var3 != null && var3.isChangingLayout()) {
            return true;
         }

         int var2 = var4.getChildCount();

         for(int var1 = 0; var1 < var2; ++var1) {
            if (hasRunningChangingLayoutTransition(var4.getChildAt(var1))) {
               return true;
            }
         }
      }

      return false;
   }

   boolean mayHaveInterferingAnimations() {
      return (!this.arePagesLaidOutContiguously() || this.mLayoutManager.getChildCount() <= 1) && this.hasRunningChangingLayoutTransition();
   }
}
