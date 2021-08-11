package android.support.v7.widget;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;

public class PagerSnapHelper extends SnapHelper {
   private static final int MAX_SCROLL_ON_FLING_DURATION = 100;
   @Nullable
   private OrientationHelper mHorizontalHelper;
   @Nullable
   private OrientationHelper mVerticalHelper;

   private int distanceToCenter(@NonNull RecyclerView.LayoutManager var1, @NonNull View var2, OrientationHelper var3) {
      int var5 = var3.getDecoratedStart(var2);
      int var6 = var3.getDecoratedMeasurement(var2) / 2;
      int var4;
      if (var1.getClipToPadding()) {
         var4 = var3.getStartAfterPadding() + var3.getTotalSpace() / 2;
      } else {
         var4 = var3.getEnd() / 2;
      }

      return var5 + var6 - var4;
   }

   @Nullable
   private View findCenterView(RecyclerView.LayoutManager var1, OrientationHelper var2) {
      int var7 = var1.getChildCount();
      if (var7 == 0) {
         return null;
      } else {
         View var8 = null;
         int var3;
         if (var1.getClipToPadding()) {
            var3 = var2.getStartAfterPadding() + var2.getTotalSpace() / 2;
         } else {
            var3 = var2.getEnd() / 2;
         }

         int var5 = Integer.MAX_VALUE;

         for(int var4 = 0; var4 < var7; ++var4) {
            View var9 = var1.getChildAt(var4);
            int var6 = Math.abs(var2.getDecoratedStart(var9) + var2.getDecoratedMeasurement(var9) / 2 - var3);
            if (var6 < var5) {
               var5 = var6;
               var8 = var9;
            }
         }

         return var8;
      }
   }

   @Nullable
   private View findStartView(RecyclerView.LayoutManager var1, OrientationHelper var2) {
      int var6 = var1.getChildCount();
      if (var6 == 0) {
         return null;
      } else {
         View var7 = null;
         int var4 = Integer.MAX_VALUE;

         for(int var3 = 0; var3 < var6; ++var3) {
            View var8 = var1.getChildAt(var3);
            int var5 = var2.getDecoratedStart(var8);
            if (var5 < var4) {
               var4 = var5;
               var7 = var8;
            }
         }

         return var7;
      }
   }

   @NonNull
   private OrientationHelper getHorizontalHelper(@NonNull RecyclerView.LayoutManager var1) {
      OrientationHelper var2 = this.mHorizontalHelper;
      if (var2 == null || var2.mLayoutManager != var1) {
         this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(var1);
      }

      return this.mHorizontalHelper;
   }

   @NonNull
   private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager var1) {
      OrientationHelper var2 = this.mVerticalHelper;
      if (var2 == null || var2.mLayoutManager != var1) {
         this.mVerticalHelper = OrientationHelper.createVerticalHelper(var1);
      }

      return this.mVerticalHelper;
   }

   @Nullable
   public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager var1, @NonNull View var2) {
      int[] var3 = new int[2];
      if (var1.canScrollHorizontally()) {
         var3[0] = this.distanceToCenter(var1, var2, this.getHorizontalHelper(var1));
      } else {
         var3[0] = 0;
      }

      if (var1.canScrollVertically()) {
         var3[1] = this.distanceToCenter(var1, var2, this.getVerticalHelper(var1));
         return var3;
      } else {
         var3[1] = 0;
         return var3;
      }
   }

   protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager var1) {
      return !(var1 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) ? null : new LinearSmoothScroller(this.mRecyclerView.getContext()) {
         protected float calculateSpeedPerPixel(DisplayMetrics var1) {
            return 100.0F / (float)var1.densityDpi;
         }

         protected int calculateTimeForScrolling(int var1) {
            return Math.min(100, super.calculateTimeForScrolling(var1));
         }

         protected void onTargetFound(View var1, RecyclerView.State var2, RecyclerView.SmoothScroller.Action var3) {
            PagerSnapHelper var8 = PagerSnapHelper.this;
            int[] var7 = var8.calculateDistanceToFinalSnap(var8.mRecyclerView.getLayoutManager(), var1);
            int var4 = var7[0];
            int var5 = var7[1];
            int var6 = this.calculateTimeForDeceleration(Math.max(Math.abs(var4), Math.abs(var5)));
            if (var6 > 0) {
               var3.update(var4, var5, var6, this.mDecelerateInterpolator);
            }
         }
      };
   }

   @Nullable
   public View findSnapView(RecyclerView.LayoutManager var1) {
      if (var1.canScrollVertically()) {
         return this.findCenterView(var1, this.getVerticalHelper(var1));
      } else {
         return var1.canScrollHorizontally() ? this.findCenterView(var1, this.getHorizontalHelper(var1)) : null;
      }
   }

   public int findTargetSnapPosition(RecyclerView.LayoutManager var1, int var2, int var3) {
      int var6 = var1.getItemCount();
      if (var6 == 0) {
         return -1;
      } else {
         View var8 = null;
         if (var1.canScrollVertically()) {
            var8 = this.findStartView(var1, this.getVerticalHelper(var1));
         } else if (var1.canScrollHorizontally()) {
            var8 = this.findStartView(var1, this.getHorizontalHelper(var1));
         }

         if (var8 == null) {
            return -1;
         } else {
            int var5 = var1.getPosition(var8);
            if (var5 == -1) {
               return -1;
            } else {
               boolean var7 = var1.canScrollHorizontally();
               boolean var4 = false;
               boolean var10;
               if (var7) {
                  if (var2 > 0) {
                     var10 = true;
                  } else {
                     var10 = false;
                  }
               } else if (var3 > 0) {
                  var10 = true;
               } else {
                  var10 = false;
               }

               boolean var11 = false;
               if (var1 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) {
                  PointF var9 = ((RecyclerView.SmoothScroller.ScrollVectorProvider)var1).computeScrollVectorForPosition(var6 - 1);
                  if (var9 != null) {
                     if (var9.x >= 0.0F && var9.y >= 0.0F) {
                        var11 = var4;
                     } else {
                        var11 = true;
                     }
                  }
               }

               if (var11) {
                  if (var10) {
                     return var5 - 1;
                  }
               } else if (var10) {
                  return var5 + 1;
               }

               return var5;
            }
         }
      }
   }
}
