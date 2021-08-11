package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.Arrays;

public class GridLayoutManager extends LinearLayoutManager {
   private static final boolean DEBUG = false;
   public static final int DEFAULT_SPAN_COUNT = -1;
   private static final String TAG = "GridLayoutManager";
   int[] mCachedBorders;
   final Rect mDecorInsets = new Rect();
   boolean mPendingSpanCountChange = false;
   final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
   final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
   View[] mSet;
   int mSpanCount = -1;
   GridLayoutManager.SpanSizeLookup mSpanSizeLookup = new GridLayoutManager.DefaultSpanSizeLookup();
   private boolean mUsingSpansToEstimateScrollBarDimensions;

   public GridLayoutManager(Context var1, int var2) {
      super(var1);
      this.setSpanCount(var2);
   }

   public GridLayoutManager(Context var1, int var2, int var3, boolean var4) {
      super(var1, var3, var4);
      this.setSpanCount(var2);
   }

   public GridLayoutManager(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.setSpanCount(getProperties(var1, var2, var3, var4).spanCount);
   }

   private void assignSpans(RecyclerView.Recycler var1, RecyclerView.State var2, int var3, boolean var4) {
      int var5;
      byte var6;
      if (var4) {
         byte var7 = 0;
         var5 = var3;
         var6 = 1;
         var3 = var7;
      } else {
         --var3;
         var5 = -1;
         var6 = -1;
      }

      for(int var10 = 0; var3 != var5; var3 += var6) {
         View var8 = this.mSet[var3];
         GridLayoutManager.LayoutParams var9 = (GridLayoutManager.LayoutParams)var8.getLayoutParams();
         var9.mSpanSize = this.getSpanSize(var1, var2, this.getPosition(var8));
         var9.mSpanIndex = var10;
         var10 += var9.mSpanSize;
      }

   }

   private void cachePreLayoutSpanMapping() {
      int var2 = this.getChildCount();

      for(int var1 = 0; var1 < var2; ++var1) {
         GridLayoutManager.LayoutParams var4 = (GridLayoutManager.LayoutParams)this.getChildAt(var1).getLayoutParams();
         int var3 = var4.getViewLayoutPosition();
         this.mPreLayoutSpanSizeCache.put(var3, var4.getSpanSize());
         this.mPreLayoutSpanIndexCache.put(var3, var4.getSpanIndex());
      }

   }

   private void calculateItemBorders(int var1) {
      this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, var1);
   }

   static int[] calculateItemBorders(int[] var0, int var1, int var2) {
      int[] var10;
      label26: {
         if (var0 != null && var0.length == var1 + 1) {
            var10 = var0;
            if (var0[var0.length - 1] == var2) {
               break label26;
            }
         }

         var10 = new int[var1 + 1];
      }

      var10[0] = 0;
      int var7 = var2 / var1;
      int var9 = var2 % var1;
      int var4 = 0;
      var2 = 0;

      for(int var3 = 1; var3 <= var1; ++var3) {
         int var8 = var2 + var9;
         var2 = var8;
         int var6 = var7;
         if (var8 > 0) {
            var2 = var8;
            var6 = var7;
            if (var1 - var8 < var9) {
               var6 = var7 + 1;
               var2 = var8 - var1;
            }
         }

         var4 += var6;
         var10[var3] = var4;
      }

      return var10;
   }

   private void clearPreLayoutSpanMappingCache() {
      this.mPreLayoutSpanSizeCache.clear();
      this.mPreLayoutSpanIndexCache.clear();
   }

   private int computeScrollOffsetWithSpanInfo(RecyclerView.State var1) {
      if (this.getChildCount() != 0) {
         if (var1.getItemCount() == 0) {
            return 0;
         } else {
            this.ensureLayoutState();
            boolean var7 = this.isSmoothScrollbarEnabled();
            View var8 = this.findFirstVisibleChildClosestToStart(var7 ^ true, true);
            View var9 = this.findFirstVisibleChildClosestToEnd(var7 ^ true, true);
            if (var8 != null) {
               if (var9 == null) {
                  return 0;
               } else {
                  int var4 = this.mSpanSizeLookup.getCachedSpanGroupIndex(this.getPosition(var8), this.mSpanCount);
                  int var5 = this.mSpanSizeLookup.getCachedSpanGroupIndex(this.getPosition(var9), this.mSpanCount);
                  int var3 = Math.min(var4, var5);
                  var4 = Math.max(var4, var5);
                  var5 = this.mSpanSizeLookup.getCachedSpanGroupIndex(var1.getItemCount() - 1, this.mSpanCount);
                  if (this.mShouldReverseLayout) {
                     var3 = Math.max(0, var5 + 1 - var4 - 1);
                  } else {
                     var3 = Math.max(0, var3);
                  }

                  if (!var7) {
                     return var3;
                  } else {
                     var4 = Math.abs(this.mOrientationHelper.getDecoratedEnd(var9) - this.mOrientationHelper.getDecoratedStart(var8));
                     var5 = this.mSpanSizeLookup.getCachedSpanGroupIndex(this.getPosition(var8), this.mSpanCount);
                     int var6 = this.mSpanSizeLookup.getCachedSpanGroupIndex(this.getPosition(var9), this.mSpanCount);
                     float var2 = (float)var4 / (float)(var6 - var5 + 1);
                     return Math.round((float)var3 * var2 + (float)(this.mOrientationHelper.getStartAfterPadding() - this.mOrientationHelper.getDecoratedStart(var8)));
                  }
               }
            } else {
               return 0;
            }
         }
      } else {
         return 0;
      }
   }

   private int computeScrollRangeWithSpanInfo(RecyclerView.State var1) {
      if (this.getChildCount() != 0) {
         if (var1.getItemCount() == 0) {
            return 0;
         } else {
            this.ensureLayoutState();
            View var7 = this.findFirstVisibleChildClosestToStart(this.isSmoothScrollbarEnabled() ^ true, true);
            View var8 = this.findFirstVisibleChildClosestToEnd(this.isSmoothScrollbarEnabled() ^ true, true);
            if (var7 != null) {
               if (var8 == null) {
                  return 0;
               } else if (!this.isSmoothScrollbarEnabled()) {
                  return this.mSpanSizeLookup.getCachedSpanGroupIndex(var1.getItemCount() - 1, this.mSpanCount) + 1;
               } else {
                  int var2 = this.mOrientationHelper.getDecoratedEnd(var8);
                  int var3 = this.mOrientationHelper.getDecoratedStart(var7);
                  int var4 = this.mSpanSizeLookup.getCachedSpanGroupIndex(this.getPosition(var7), this.mSpanCount);
                  int var5 = this.mSpanSizeLookup.getCachedSpanGroupIndex(this.getPosition(var8), this.mSpanCount);
                  int var6 = this.mSpanSizeLookup.getCachedSpanGroupIndex(var1.getItemCount() - 1, this.mSpanCount);
                  return (int)((float)(var2 - var3) / (float)(var5 - var4 + 1) * (float)(var6 + 1));
               }
            } else {
               return 0;
            }
         }
      } else {
         return 0;
      }
   }

   private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.AnchorInfo var3, int var4) {
      boolean var5;
      if (var4 == 1) {
         var5 = true;
      } else {
         var5 = false;
      }

      var4 = this.getSpanIndex(var1, var2, var3.mPosition);
      if (var5) {
         while(var4 > 0 && var3.mPosition > 0) {
            --var3.mPosition;
            var4 = this.getSpanIndex(var1, var2, var3.mPosition);
         }
      } else {
         int var7 = var2.getItemCount();
         int var6 = var3.mPosition;
         int var8 = var4;

         for(var4 = var6; var4 < var7 - 1; var8 = var6) {
            var6 = this.getSpanIndex(var1, var2, var4 + 1);
            if (var6 <= var8) {
               break;
            }

            ++var4;
         }

         var3.mPosition = var4;
      }

   }

   private void ensureViewSet() {
      View[] var1 = this.mSet;
      if (var1 == null || var1.length != this.mSpanCount) {
         this.mSet = new View[this.mSpanCount];
      }

   }

   private int getSpanGroupIndex(RecyclerView.Recycler var1, RecyclerView.State var2, int var3) {
      if (!var2.isPreLayout()) {
         return this.mSpanSizeLookup.getCachedSpanGroupIndex(var3, this.mSpanCount);
      } else {
         int var4 = var1.convertPreLayoutPositionToPostLayout(var3);
         if (var4 == -1) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Cannot find span size for pre layout position. ");
            var5.append(var3);
            Log.w("GridLayoutManager", var5.toString());
            return 0;
         } else {
            return this.mSpanSizeLookup.getCachedSpanGroupIndex(var4, this.mSpanCount);
         }
      }
   }

   private int getSpanIndex(RecyclerView.Recycler var1, RecyclerView.State var2, int var3) {
      if (!var2.isPreLayout()) {
         return this.mSpanSizeLookup.getCachedSpanIndex(var3, this.mSpanCount);
      } else {
         int var4 = this.mPreLayoutSpanIndexCache.get(var3, -1);
         if (var4 != -1) {
            return var4;
         } else {
            var4 = var1.convertPreLayoutPositionToPostLayout(var3);
            if (var4 == -1) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
               var5.append(var3);
               Log.w("GridLayoutManager", var5.toString());
               return 0;
            } else {
               return this.mSpanSizeLookup.getCachedSpanIndex(var4, this.mSpanCount);
            }
         }
      }
   }

   private int getSpanSize(RecyclerView.Recycler var1, RecyclerView.State var2, int var3) {
      if (!var2.isPreLayout()) {
         return this.mSpanSizeLookup.getSpanSize(var3);
      } else {
         int var4 = this.mPreLayoutSpanSizeCache.get(var3, -1);
         if (var4 != -1) {
            return var4;
         } else {
            var4 = var1.convertPreLayoutPositionToPostLayout(var3);
            if (var4 == -1) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
               var5.append(var3);
               Log.w("GridLayoutManager", var5.toString());
               return 1;
            } else {
               return this.mSpanSizeLookup.getSpanSize(var4);
            }
         }
      }
   }

   private void guessMeasurement(float var1, int var2) {
      this.calculateItemBorders(Math.max(Math.round((float)this.mSpanCount * var1), var2));
   }

   private void measureChild(View var1, int var2, boolean var3) {
      GridLayoutManager.LayoutParams var7 = (GridLayoutManager.LayoutParams)var1.getLayoutParams();
      Rect var8 = var7.mDecorInsets;
      int var4 = var8.top + var8.bottom + var7.topMargin + var7.bottomMargin;
      int var5 = var8.left + var8.right + var7.leftMargin + var7.rightMargin;
      int var6 = this.getSpaceForSpanRange(var7.mSpanIndex, var7.mSpanSize);
      if (this.mOrientation == 1) {
         var2 = getChildMeasureSpec(var6, var2, var5, var7.width, false);
         var4 = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), this.getHeightMode(), var4, var7.height, true);
      } else {
         var4 = getChildMeasureSpec(var6, var2, var4, var7.height, false);
         var2 = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), this.getWidthMode(), var5, var7.width, true);
      }

      this.measureChildWithDecorationsAndMargin(var1, var2, var4, var3);
   }

   private void measureChildWithDecorationsAndMargin(View var1, int var2, int var3, boolean var4) {
      RecyclerView.LayoutParams var5 = (RecyclerView.LayoutParams)var1.getLayoutParams();
      if (var4) {
         var4 = this.shouldReMeasureChild(var1, var2, var3, var5);
      } else {
         var4 = this.shouldMeasureChild(var1, var2, var3, var5);
      }

      if (var4) {
         var1.measure(var2, var3);
      }

   }

   private void updateMeasurements() {
      int var1;
      if (this.getOrientation() == 1) {
         var1 = this.getWidth() - this.getPaddingRight() - this.getPaddingLeft();
      } else {
         var1 = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
      }

      this.calculateItemBorders(var1);
   }

   public boolean checkLayoutParams(RecyclerView.LayoutParams var1) {
      return var1 instanceof GridLayoutManager.LayoutParams;
   }

   void collectPrefetchPositionsForLayoutState(RecyclerView.State var1, LinearLayoutManager.LayoutState var2, RecyclerView.LayoutManager.LayoutPrefetchRegistry var3) {
      int var5 = this.mSpanCount;

      for(int var4 = 0; var4 < this.mSpanCount && var2.hasMore(var1) && var5 > 0; ++var4) {
         int var6 = var2.mCurrentPosition;
         var3.addPosition(var6, Math.max(0, var2.mScrollingOffset));
         var5 -= this.mSpanSizeLookup.getSpanSize(var6);
         var2.mCurrentPosition += var2.mItemDirection;
      }

   }

   public int computeHorizontalScrollOffset(RecyclerView.State var1) {
      return this.mUsingSpansToEstimateScrollBarDimensions ? this.computeScrollOffsetWithSpanInfo(var1) : super.computeHorizontalScrollOffset(var1);
   }

   public int computeHorizontalScrollRange(RecyclerView.State var1) {
      return this.mUsingSpansToEstimateScrollBarDimensions ? this.computeScrollRangeWithSpanInfo(var1) : super.computeHorizontalScrollRange(var1);
   }

   public int computeVerticalScrollOffset(RecyclerView.State var1) {
      return this.mUsingSpansToEstimateScrollBarDimensions ? this.computeScrollOffsetWithSpanInfo(var1) : super.computeVerticalScrollOffset(var1);
   }

   public int computeVerticalScrollRange(RecyclerView.State var1) {
      return this.mUsingSpansToEstimateScrollBarDimensions ? this.computeScrollRangeWithSpanInfo(var1) : super.computeVerticalScrollRange(var1);
   }

   View findReferenceChild(RecyclerView.Recycler var1, RecyclerView.State var2, int var3, int var4, int var5) {
      this.ensureLayoutState();
      View var11 = null;
      View var10 = null;
      int var7 = this.mOrientationHelper.getStartAfterPadding();
      int var8 = this.mOrientationHelper.getEndAfterPadding();
      byte var6;
      if (var4 > var3) {
         var6 = 1;
      } else {
         var6 = -1;
      }

      while(var3 != var4) {
         View var12 = this.getChildAt(var3);
         int var9 = this.getPosition(var12);
         View var13 = var11;
         View var14 = var10;
         if (var9 >= 0) {
            var13 = var11;
            var14 = var10;
            if (var9 < var5) {
               if (this.getSpanIndex(var1, var2, var9) != 0) {
                  var13 = var11;
                  var14 = var10;
               } else if (((RecyclerView.LayoutParams)var12.getLayoutParams()).isItemRemoved()) {
                  var13 = var11;
                  var14 = var10;
                  if (var11 == null) {
                     var13 = var12;
                     var14 = var10;
                  }
               } else {
                  if (this.mOrientationHelper.getDecoratedStart(var12) < var8 && this.mOrientationHelper.getDecoratedEnd(var12) >= var7) {
                     return var12;
                  }

                  var13 = var11;
                  var14 = var10;
                  if (var10 == null) {
                     var14 = var12;
                     var13 = var11;
                  }
               }
            }
         }

         var3 += var6;
         var11 = var13;
         var10 = var14;
      }

      if (var10 != null) {
         return var10;
      } else {
         return var11;
      }
   }

   public RecyclerView.LayoutParams generateDefaultLayoutParams() {
      return this.mOrientation == 0 ? new GridLayoutManager.LayoutParams(-2, -1) : new GridLayoutManager.LayoutParams(-1, -2);
   }

   public RecyclerView.LayoutParams generateLayoutParams(Context var1, AttributeSet var2) {
      return new GridLayoutManager.LayoutParams(var1, var2);
   }

   public RecyclerView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof MarginLayoutParams ? new GridLayoutManager.LayoutParams((MarginLayoutParams)var1) : new GridLayoutManager.LayoutParams(var1);
   }

   public int getColumnCountForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
      if (this.mOrientation == 1) {
         return this.mSpanCount;
      } else {
         return var2.getItemCount() < 1 ? 0 : this.getSpanGroupIndex(var1, var2, var2.getItemCount() - 1) + 1;
      }
   }

   public int getRowCountForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
      if (this.mOrientation == 0) {
         return this.mSpanCount;
      } else {
         return var2.getItemCount() < 1 ? 0 : this.getSpanGroupIndex(var1, var2, var2.getItemCount() - 1) + 1;
      }
   }

   int getSpaceForSpanRange(int var1, int var2) {
      int[] var4;
      if (this.mOrientation == 1 && this.isLayoutRTL()) {
         var4 = this.mCachedBorders;
         int var3 = this.mSpanCount;
         return var4[var3 - var1] - var4[var3 - var1 - var2];
      } else {
         var4 = this.mCachedBorders;
         return var4[var1 + var2] - var4[var1];
      }
   }

   public int getSpanCount() {
      return this.mSpanCount;
   }

   public GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
      return this.mSpanSizeLookup;
   }

   public boolean isUsingSpansToEstimateScrollbarDimensions() {
      return this.mUsingSpansToEstimateScrollBarDimensions;
   }

   void layoutChunk(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.LayoutState var3, LinearLayoutManager.LayoutChunkResult var4) {
      int var14 = this.mOrientationHelper.getModeInOther();
      boolean var8;
      if (var14 != 1073741824) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var12;
      if (this.getChildCount() > 0) {
         var12 = this.mCachedBorders[this.mSpanCount];
      } else {
         var12 = 0;
      }

      if (var8) {
         this.updateMeasurements();
      }

      boolean var16;
      if (var3.mItemDirection == 1) {
         var16 = true;
      } else {
         var16 = false;
      }

      int var9 = this.mSpanCount;
      int var10;
      int var21;
      if (!var16) {
         var9 = this.getSpanIndex(var1, var2, var3.mCurrentPosition) + this.getSpanSize(var1, var2, var3.mCurrentPosition);
         var21 = 0;
         var10 = 0;
      } else {
         var21 = 0;
         var10 = 0;
      }

      int var13;
      int var15;
      while(var21 < this.mSpanCount && var3.hasMore(var2) && var9 > 0) {
         var15 = var3.mCurrentPosition;
         var13 = this.getSpanSize(var1, var2, var15);
         if (var13 > this.mSpanCount) {
            StringBuilder var18 = new StringBuilder();
            var18.append("Item at position ");
            var18.append(var15);
            var18.append(" requires ");
            var18.append(var13);
            var18.append(" spans but GridLayoutManager has only ");
            var18.append(this.mSpanCount);
            var18.append(" spans.");
            throw new IllegalArgumentException(var18.toString());
         }

         var9 -= var13;
         if (var9 < 0) {
            break;
         }

         View var17 = var3.next(var1);
         if (var17 == null) {
            break;
         }

         var10 += var13;
         this.mSet[var21] = var17;
         ++var21;
      }

      if (var21 == 0) {
         var4.mFinished = true;
      } else {
         var9 = 0;
         this.assignSpans(var1, var2, var21, var16);
         var13 = 0;

         float var5;
         float var6;
         View var19;
         GridLayoutManager.LayoutParams var20;
         for(var5 = 0.0F; var13 < var21; var5 = var6) {
            var19 = this.mSet[var13];
            if (var3.mScrapList == null) {
               if (var16) {
                  this.addView(var19);
               } else {
                  this.addView(var19, 0);
               }
            } else if (var16) {
               this.addDisappearingView(var19);
            } else {
               this.addDisappearingView(var19, 0);
            }

            this.calculateItemDecorationsForChild(var19, this.mDecorInsets);
            this.measureChild(var19, var14, false);
            var15 = this.mOrientationHelper.getDecoratedMeasurement(var19);
            var10 = var9;
            if (var15 > var9) {
               var10 = var15;
            }

            var20 = (GridLayoutManager.LayoutParams)var19.getLayoutParams();
            float var7 = (float)this.mOrientationHelper.getDecoratedMeasurementInOther(var19) * 1.0F / (float)var20.mSpanSize;
            var6 = var5;
            if (var7 > var5) {
               var6 = var7;
            }

            ++var13;
            var9 = var10;
         }

         if (var8) {
            this.guessMeasurement(var5, var12);
            var9 = 0;

            for(var10 = 0; var10 < var21; var9 = var12) {
               var19 = this.mSet[var10];
               this.measureChild(var19, 1073741824, true);
               var13 = this.mOrientationHelper.getDecoratedMeasurement(var19);
               var12 = var9;
               if (var13 > var9) {
                  var12 = var13;
               }

               ++var10;
            }
         }

         for(var12 = 0; var12 < var21; ++var12) {
            var19 = this.mSet[var12];
            if (this.mOrientationHelper.getDecoratedMeasurement(var19) != var9) {
               var20 = (GridLayoutManager.LayoutParams)var19.getLayoutParams();
               Rect var24 = var20.mDecorInsets;
               var14 = var24.top + var24.bottom + var20.topMargin + var20.bottomMargin;
               var13 = var24.left + var24.right + var20.leftMargin + var20.rightMargin;
               var15 = this.getSpaceForSpanRange(var20.mSpanIndex, var20.mSpanSize);
               if (this.mOrientation == 1) {
                  var13 = getChildMeasureSpec(var15, 1073741824, var13, var20.width, false);
                  var14 = MeasureSpec.makeMeasureSpec(var9 - var14, 1073741824);
               } else {
                  var13 = MeasureSpec.makeMeasureSpec(var9 - var13, 1073741824);
                  var14 = getChildMeasureSpec(var15, 1073741824, var14, var20.height, false);
               }

               this.measureChildWithDecorationsAndMargin(var19, var13, var14, true);
            }
         }

         var4.mConsumed = var9;
         byte var23 = 0;
         var10 = 0;
         int var22 = 0;
         var12 = 0;
         if (this.mOrientation == 1) {
            if (var3.mLayoutDirection == -1) {
               var12 = var3.mOffset;
               var22 = var12 - var9;
               var9 = var23;
            } else {
               var22 = var3.mOffset;
               var12 = var22 + var9;
               var9 = var23;
            }
         } else if (var3.mLayoutDirection == -1) {
            var10 = var3.mOffset;
            var9 = var10 - var9;
         } else {
            var13 = var3.mOffset;
            var10 = var13 + var9;
            var9 = var13;
         }

         var14 = 0;
         var13 = var21;

         for(var21 = var12; var14 < var13; var21 = var12) {
            var19 = this.mSet[var14];
            var20 = (GridLayoutManager.LayoutParams)var19.getLayoutParams();
            if (this.mOrientation == 1) {
               if (this.isLayoutRTL()) {
                  var10 = this.getPaddingLeft() + this.mCachedBorders[this.mSpanCount - var20.mSpanIndex];
                  var12 = var10 - this.mOrientationHelper.getDecoratedMeasurementInOther(var19);
                  var9 = var22;
                  var22 = var21;
                  var21 = var9;
                  var9 = var12;
               } else {
                  var10 = this.getPaddingLeft() + this.mCachedBorders[var20.mSpanIndex];
                  var12 = this.mOrientationHelper.getDecoratedMeasurementInOther(var19);
                  var9 = var10;
                  var12 += var10;
                  var10 = var22;
                  var22 = var21;
                  var21 = var10;
                  var10 = var12;
               }
            } else {
               var22 = this.getPaddingTop() + this.mCachedBorders[var20.mSpanIndex];
               var12 = this.mOrientationHelper.getDecoratedMeasurementInOther(var19);
               var21 = var22;
               var22 += var12;
            }

            this.layoutDecoratedWithMargins(var19, var9, var21, var10, var22);
            if (var20.isItemRemoved() || var20.isItemChanged()) {
               var4.mIgnoreConsumed = true;
            }

            var4.mFocusable |= var19.hasFocusable();
            ++var14;
            var12 = var22;
            var22 = var21;
         }

         Arrays.fill(this.mSet, (Object)null);
      }
   }

   void onAnchorReady(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.AnchorInfo var3, int var4) {
      super.onAnchorReady(var1, var2, var3, var4);
      this.updateMeasurements();
      if (var2.getItemCount() > 0 && !var2.isPreLayout()) {
         this.ensureAnchorIsInCorrectSpan(var1, var2, var3, var4);
      }

      this.ensureViewSet();
   }

   public View onFocusSearchFailed(View var1, int var2, RecyclerView.Recycler var3, RecyclerView.State var4) {
      View var23 = this.findContainingItemView(var1);
      if (var23 == null) {
         return null;
      } else {
         GridLayoutManager.LayoutParams var21 = (GridLayoutManager.LayoutParams)var23.getLayoutParams();
         int var15 = var21.mSpanIndex;
         int var16 = var21.mSpanIndex + var21.mSpanSize;
         if (super.onFocusSearchFailed(var1, var2, var3, var4) == null) {
            return null;
         } else {
            boolean var20;
            if (this.convertFocusDirectionToLayoutDirection(var2) == 1) {
               var20 = true;
            } else {
               var20 = false;
            }

            boolean var25;
            if (var20 != this.mShouldReverseLayout) {
               var25 = true;
            } else {
               var25 = false;
            }

            byte var7;
            int var8;
            if (var25) {
               var2 = this.getChildCount() - 1;
               var7 = -1;
               var8 = -1;
            } else {
               var2 = 0;
               var7 = 1;
               var8 = this.getChildCount();
            }

            boolean var9;
            if (this.mOrientation == 1 && this.isLayoutRTL()) {
               var9 = true;
            } else {
               var9 = false;
            }

            View var22 = null;
            View var26 = null;
            int var11 = this.getSpanGroupIndex(var3, var4, var2);
            int var6 = -1;
            int var5 = 0;
            int var14 = -1;
            int var13 = 0;
            int var12 = var2;

            for(var1 = var23; var12 != var8; var12 += var7) {
               var2 = this.getSpanGroupIndex(var3, var4, var12);
               var23 = this.getChildAt(var12);
               if (var23 == var1) {
                  break;
               }

               if (var23.hasFocusable() && var2 != var11) {
                  if (var22 != null) {
                     break;
                  }
               } else {
                  GridLayoutManager.LayoutParams var24 = (GridLayoutManager.LayoutParams)var23.getLayoutParams();
                  int var17 = var24.mSpanIndex;
                  int var18 = var24.mSpanIndex + var24.mSpanSize;
                  if (var23.hasFocusable() && var17 == var15 && var18 == var16) {
                     return var23;
                  }

                  if ((!var23.hasFocusable() || var22 != null) && (var23.hasFocusable() || var26 != null)) {
                     label138: {
                        var2 = Math.max(var17, var15);
                        int var19 = Math.min(var18, var16) - var2;
                        if (var23.hasFocusable()) {
                           if (var19 > var5) {
                              var25 = true;
                              break label138;
                           }

                           if (var19 == var5) {
                              if (var17 > var6) {
                                 var25 = true;
                              } else {
                                 var25 = false;
                              }

                              if (var9 == var25) {
                                 var25 = true;
                                 break label138;
                              }
                           }
                        } else if (var22 == null) {
                           var25 = false;
                           if (this.isViewPartiallyVisible(var23, false, true)) {
                              if (var19 > var13) {
                                 var25 = true;
                                 break label138;
                              }

                              if (var19 == var13) {
                                 if (var17 > var14) {
                                    var25 = true;
                                 }

                                 if (var9 == var25) {
                                    var25 = true;
                                    break label138;
                                 }
                              }
                           }
                        }

                        var25 = false;
                     }
                  } else {
                     var25 = true;
                  }

                  if (var25) {
                     if (var23.hasFocusable()) {
                        var6 = var24.mSpanIndex;
                        var2 = Math.min(var18, var16);
                        var5 = Math.max(var17, var15);
                        var22 = var23;
                        var5 = var2 - var5;
                     } else {
                        var14 = var24.mSpanIndex;
                        var2 = Math.min(var18, var16);
                        var13 = Math.max(var17, var15);
                        var26 = var23;
                        var13 = var2 - var13;
                     }
                  }
               }
            }

            return var22 != null ? var22 : var26;
         }
      }
   }

   public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler var1, RecyclerView.State var2, View var3, AccessibilityNodeInfoCompat var4) {
      android.view.ViewGroup.LayoutParams var6 = var3.getLayoutParams();
      if (!(var6 instanceof GridLayoutManager.LayoutParams)) {
         super.onInitializeAccessibilityNodeInfoForItem(var3, var4);
      } else {
         GridLayoutManager.LayoutParams var7 = (GridLayoutManager.LayoutParams)var6;
         int var5 = this.getSpanGroupIndex(var1, var2, var7.getViewLayoutPosition());
         if (this.mOrientation == 0) {
            var4.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(var7.getSpanIndex(), var7.getSpanSize(), var5, 1, false, false));
         } else {
            var4.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(var5, 1, var7.getSpanIndex(), var7.getSpanSize(), false, false));
         }
      }
   }

   public void onItemsAdded(RecyclerView var1, int var2, int var3) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
      this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
   }

   public void onItemsChanged(RecyclerView var1) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
      this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
   }

   public void onItemsMoved(RecyclerView var1, int var2, int var3, int var4) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
      this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
   }

   public void onItemsRemoved(RecyclerView var1, int var2, int var3) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
      this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
   }

   public void onItemsUpdated(RecyclerView var1, int var2, int var3, Object var4) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
      this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
   }

   public void onLayoutChildren(RecyclerView.Recycler var1, RecyclerView.State var2) {
      if (var2.isPreLayout()) {
         this.cachePreLayoutSpanMapping();
      }

      super.onLayoutChildren(var1, var2);
      this.clearPreLayoutSpanMappingCache();
   }

   public void onLayoutCompleted(RecyclerView.State var1) {
      super.onLayoutCompleted(var1);
      this.mPendingSpanCountChange = false;
   }

   public int scrollHorizontallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      this.updateMeasurements();
      this.ensureViewSet();
      return super.scrollHorizontallyBy(var1, var2, var3);
   }

   public int scrollVerticallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      this.updateMeasurements();
      this.ensureViewSet();
      return super.scrollVerticallyBy(var1, var2, var3);
   }

   public void setMeasuredDimension(Rect var1, int var2, int var3) {
      if (this.mCachedBorders == null) {
         super.setMeasuredDimension(var1, var2, var3);
      }

      int var4 = this.getPaddingLeft() + this.getPaddingRight();
      int var5 = this.getPaddingTop() + this.getPaddingBottom();
      int[] var6;
      if (this.mOrientation == 1) {
         var3 = chooseSize(var3, var1.height() + var5, this.getMinimumHeight());
         var6 = this.mCachedBorders;
         var2 = chooseSize(var2, var6[var6.length - 1] + var4, this.getMinimumWidth());
      } else {
         var2 = chooseSize(var2, var1.width() + var4, this.getMinimumWidth());
         var6 = this.mCachedBorders;
         var3 = chooseSize(var3, var6[var6.length - 1] + var5, this.getMinimumHeight());
      }

      this.setMeasuredDimension(var2, var3);
   }

   public void setSpanCount(int var1) {
      if (var1 != this.mSpanCount) {
         this.mPendingSpanCountChange = true;
         if (var1 >= 1) {
            this.mSpanCount = var1;
            this.mSpanSizeLookup.invalidateSpanIndexCache();
            this.requestLayout();
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Span count should be at least 1. Provided ");
            var2.append(var1);
            throw new IllegalArgumentException(var2.toString());
         }
      }
   }

   public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup var1) {
      this.mSpanSizeLookup = var1;
   }

   public void setStackFromEnd(boolean var1) {
      if (!var1) {
         super.setStackFromEnd(false);
      } else {
         throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
      }
   }

   public void setUsingSpansToEstimateScrollbarDimensions(boolean var1) {
      this.mUsingSpansToEstimateScrollBarDimensions = var1;
   }

   public boolean supportsPredictiveItemAnimations() {
      return this.mPendingSavedState == null && !this.mPendingSpanCountChange;
   }

   public static final class DefaultSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
      public int getSpanIndex(int var1, int var2) {
         return var1 % var2;
      }

      public int getSpanSize(int var1) {
         return 1;
      }
   }

   public static class LayoutParams extends RecyclerView.LayoutParams {
      public static final int INVALID_SPAN_ID = -1;
      int mSpanIndex = -1;
      int mSpanSize = 0;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      public LayoutParams(RecyclerView.LayoutParams var1) {
         super(var1);
      }

      public int getSpanIndex() {
         return this.mSpanIndex;
      }

      public int getSpanSize() {
         return this.mSpanSize;
      }
   }

   public abstract static class SpanSizeLookup {
      private boolean mCacheSpanGroupIndices = false;
      private boolean mCacheSpanIndices = false;
      final SparseIntArray mSpanGroupIndexCache = new SparseIntArray();
      final SparseIntArray mSpanIndexCache = new SparseIntArray();

      static int findFirstKeyLessThan(SparseIntArray var0, int var1) {
         int var3 = 0;
         int var2 = var0.size() - 1;

         while(var3 <= var2) {
            int var4 = var3 + var2 >>> 1;
            if (var0.keyAt(var4) < var1) {
               var3 = var4 + 1;
            } else {
               var2 = var4 - 1;
            }
         }

         var1 = var3 - 1;
         if (var1 >= 0 && var1 < var0.size()) {
            return var0.keyAt(var1);
         } else {
            return -1;
         }
      }

      int getCachedSpanGroupIndex(int var1, int var2) {
         if (!this.mCacheSpanGroupIndices) {
            return this.getSpanGroupIndex(var1, var2);
         } else {
            int var3 = this.mSpanGroupIndexCache.get(var1, -1);
            if (var3 != -1) {
               return var3;
            } else {
               var2 = this.getSpanGroupIndex(var1, var2);
               this.mSpanGroupIndexCache.put(var1, var2);
               return var2;
            }
         }
      }

      int getCachedSpanIndex(int var1, int var2) {
         if (!this.mCacheSpanIndices) {
            return this.getSpanIndex(var1, var2);
         } else {
            int var3 = this.mSpanIndexCache.get(var1, -1);
            if (var3 != -1) {
               return var3;
            } else {
               var2 = this.getSpanIndex(var1, var2);
               this.mSpanIndexCache.put(var1, var2);
               return var2;
            }
         }
      }

      public int getSpanGroupIndex(int var1, int var2) {
         byte var6 = 0;
         byte var7 = 0;
         byte var8 = 0;
         int var3 = var6;
         int var4 = var7;
         int var5 = var8;
         int var9;
         int var10;
         int var11;
         int var12;
         if (this.mCacheSpanGroupIndices) {
            var9 = findFirstKeyLessThan(this.mSpanGroupIndexCache, var1);
            var3 = var6;
            var4 = var7;
            var5 = var8;
            if (var9 != -1) {
               var11 = this.mSpanGroupIndexCache.get(var9);
               var10 = var9 + 1;
               var12 = this.getCachedSpanIndex(var9, var2) + this.getSpanSize(var9);
               var3 = var12;
               var4 = var11;
               var5 = var10;
               if (var12 == var2) {
                  var3 = 0;
                  var4 = var11 + 1;
                  var5 = var10;
               }
            }
         }

         var9 = this.getSpanSize(var1);

         for(var10 = var5; var10 < var1; var4 = var5) {
            var11 = this.getSpanSize(var10);
            var12 = var3 + var11;
            if (var12 == var2) {
               var3 = 0;
               var5 = var4 + 1;
            } else {
               var3 = var12;
               var5 = var4;
               if (var12 > var2) {
                  var3 = var11;
                  var5 = var4 + 1;
               }
            }

            ++var10;
         }

         var1 = var4;
         if (var3 + var9 > var2) {
            var1 = var4 + 1;
         }

         return var1;
      }

      public int getSpanIndex(int var1, int var2) {
         int var7 = this.getSpanSize(var1);
         if (var7 == var2) {
            return 0;
         } else {
            byte var5 = 0;
            byte var6 = 0;
            int var3 = var5;
            int var4 = var6;
            if (this.mCacheSpanIndices) {
               int var8 = findFirstKeyLessThan(this.mSpanIndexCache, var1);
               var3 = var5;
               var4 = var6;
               if (var8 >= 0) {
                  var3 = this.mSpanIndexCache.get(var8) + this.getSpanSize(var8);
                  var4 = var8 + 1;
               }
            }

            for(; var4 < var1; ++var4) {
               int var9 = this.getSpanSize(var4);
               int var10 = var3 + var9;
               if (var10 == var2) {
                  var3 = 0;
               } else {
                  var3 = var10;
                  if (var10 > var2) {
                     var3 = var9;
                  }
               }
            }

            if (var3 + var7 <= var2) {
               return var3;
            } else {
               return 0;
            }
         }
      }

      public abstract int getSpanSize(int var1);

      public void invalidateSpanGroupIndexCache() {
         this.mSpanGroupIndexCache.clear();
      }

      public void invalidateSpanIndexCache() {
         this.mSpanIndexCache.clear();
      }

      public boolean isSpanGroupIndexCacheEnabled() {
         return this.mCacheSpanGroupIndices;
      }

      public boolean isSpanIndexCacheEnabled() {
         return this.mCacheSpanIndices;
      }

      public void setSpanGroupIndexCacheEnabled(boolean var1) {
         if (!var1) {
            this.mSpanGroupIndexCache.clear();
         }

         this.mCacheSpanGroupIndices = var1;
      }

      public void setSpanIndexCacheEnabled(boolean var1) {
         if (!var1) {
            this.mSpanGroupIndexCache.clear();
         }

         this.mCacheSpanIndices = var1;
      }
   }
}
