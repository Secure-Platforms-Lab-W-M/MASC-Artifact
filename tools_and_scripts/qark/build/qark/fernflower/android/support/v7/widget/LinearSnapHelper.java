package android.support.v7.widget;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class LinearSnapHelper extends SnapHelper {
   private static final float INVALID_DISTANCE = 1.0F;
   @Nullable
   private OrientationHelper mHorizontalHelper;
   @Nullable
   private OrientationHelper mVerticalHelper;

   private float computeDistancePerChild(RecyclerView.LayoutManager var1, OrientationHelper var2) {
      View var8 = null;
      View var9 = null;
      int var3 = Integer.MAX_VALUE;
      int var4 = Integer.MIN_VALUE;
      int var7 = var1.getChildCount();
      if (var7 == 0) {
         return 1.0F;
      } else {
         int var5;
         for(var5 = 0; var5 < var7; ++var5) {
            View var10 = var1.getChildAt(var5);
            int var6 = var1.getPosition(var10);
            if (var6 != -1) {
               if (var6 < var3) {
                  var3 = var6;
                  var8 = var10;
               }

               if (var6 > var4) {
                  var4 = var6;
                  var9 = var10;
               }
            }
         }

         if (var8 != null) {
            if (var9 == null) {
               return 1.0F;
            } else {
               var5 = Math.min(var2.getDecoratedStart(var8), var2.getDecoratedStart(var9));
               var5 = Math.max(var2.getDecoratedEnd(var8), var2.getDecoratedEnd(var9)) - var5;
               if (var5 == 0) {
                  return 1.0F;
               } else {
                  return (float)var5 * 1.0F / (float)(var4 - var3 + 1);
               }
            }
         } else {
            return 1.0F;
         }
      }
   }

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

   private int estimateNextPositionDiffForFling(RecyclerView.LayoutManager var1, OrientationHelper var2, int var3, int var4) {
      int[] var6 = this.calculateScrollDistance(var3, var4);
      float var5 = this.computeDistancePerChild(var1, var2);
      if (var5 <= 0.0F) {
         return 0;
      } else {
         if (Math.abs(var6[0]) > Math.abs(var6[1])) {
            var3 = var6[0];
         } else {
            var3 = var6[1];
         }

         return Math.round((float)var3 / var5);
      }
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

   public View findSnapView(RecyclerView.LayoutManager var1) {
      if (var1.canScrollVertically()) {
         return this.findCenterView(var1, this.getVerticalHelper(var1));
      } else {
         return var1.canScrollHorizontally() ? this.findCenterView(var1, this.getHorizontalHelper(var1)) : null;
      }
   }

   public int findTargetSnapPosition(RecyclerView.LayoutManager var1, int var2, int var3) {
      if (!(var1 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
         return -1;
      } else {
         int var4 = var1.getItemCount();
         if (var4 == 0) {
            return -1;
         } else {
            View var6 = this.findSnapView(var1);
            if (var6 == null) {
               return -1;
            } else {
               int var5 = var1.getPosition(var6);
               if (var5 == -1) {
                  return -1;
               } else {
                  PointF var7 = ((RecyclerView.SmoothScroller.ScrollVectorProvider)var1).computeScrollVectorForPosition(var4 - 1);
                  if (var7 == null) {
                     return -1;
                  } else {
                     if (var1.canScrollHorizontally()) {
                        var2 = this.estimateNextPositionDiffForFling(var1, this.getHorizontalHelper(var1), var2, 0);
                        if (var7.x < 0.0F) {
                           var2 = -var2;
                        }
                     } else {
                        var2 = 0;
                     }

                     if (var1.canScrollVertically()) {
                        var3 = this.estimateNextPositionDiffForFling(var1, this.getVerticalHelper(var1), 0, var3);
                        if (var7.y < 0.0F) {
                           var3 = -var3;
                        }
                     } else {
                        var3 = 0;
                     }

                     if (var1.canScrollVertically()) {
                        var2 = var3;
                     }

                     if (var2 == 0) {
                        return -1;
                     } else {
                        var2 += var5;
                        if (var2 < 0) {
                           var2 = 0;
                        }

                        return var2 >= var4 ? var4 - 1 : var2;
                     }
                  }
               }
            }
         }
      }
   }
}
