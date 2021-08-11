package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
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

   private void assignSpans(RecyclerView.Recycler var1, RecyclerView.State var2, int var3, int var4, boolean var5) {
      byte var6;
      if (var5) {
         byte var7 = 0;
         var4 = var3;
         var6 = 1;
         var3 = var7;
      } else {
         --var3;
         var4 = -1;
         var6 = -1;
      }

      for(int var10 = 0; var3 != var4; var3 += var6) {
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
      if (var0 == null || var0.length != var1 + 1 || var0[var0.length - 1] != var2) {
         var0 = new int[var1 + 1];
      }

      var0[0] = 0;
      int var6 = var2 / var1;
      int var7 = var2 % var1;
      int var4 = 0;
      var2 = 0;

      for(int var3 = 1; var3 <= var1; ++var3) {
         int var5 = var6;
         var2 += var7;
         if (var2 > 0 && var1 - var2 < var7) {
            var5 = var6 + 1;
            var2 -= var1;
         }

         var4 += var5;
         var0[var3] = var4;
      }

      return var0;
   }

   private void clearPreLayoutSpanMappingCache() {
      this.mPreLayoutSpanSizeCache.clear();
      this.mPreLayoutSpanIndexCache.clear();
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
         return this.mSpanSizeLookup.getSpanGroupIndex(var3, this.mSpanCount);
      } else {
         int var4 = var1.convertPreLayoutPositionToPostLayout(var3);
         if (var4 == -1) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Cannot find span size for pre layout position. ");
            var5.append(var3);
            Log.w("GridLayoutManager", var5.toString());
            return 0;
         } else {
            return this.mSpanSizeLookup.getSpanGroupIndex(var4, this.mSpanCount);
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

      for(; var3 != var4; var3 += var6) {
         View var12 = this.getChildAt(var3);
         int var9 = this.getPosition(var12);
         if (var9 >= 0 && var9 < var5 && this.getSpanIndex(var1, var2, var9) == 0) {
            if (((RecyclerView.LayoutParams)var12.getLayoutParams()).isItemRemoved()) {
               if (var11 == null) {
                  var11 = var12;
               }
            } else {
               if (this.mOrientationHelper.getDecoratedStart(var12) < var8 && this.mOrientationHelper.getDecoratedEnd(var12) >= var7) {
                  return var12;
               }

               if (var10 == null) {
                  var10 = var12;
               }
            }
         }
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

   void layoutChunk(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.LayoutState var3, LinearLayoutManager.LayoutChunkResult var4) {
      int var14 = this.mOrientationHelper.getModeInOther();
      boolean var9;
      if (var14 != 1073741824) {
         var9 = true;
      } else {
         var9 = false;
      }

      int var10;
      if (this.getChildCount() > 0) {
         var10 = this.mCachedBorders[this.mSpanCount];
      } else {
         var10 = 0;
      }

      if (var9) {
         this.updateMeasurements();
      }

      boolean var15;
      if (var3.mItemDirection == 1) {
         var15 = true;
      } else {
         var15 = false;
      }

      int var7 = this.mSpanCount;
      int var8;
      int var11;
      if (!var15) {
         var7 = this.getSpanIndex(var1, var2, var3.mCurrentPosition) + this.getSpanSize(var1, var2, var3.mCurrentPosition);
         var11 = 0;
         var8 = 0;
      } else {
         var11 = 0;
         var8 = 0;
      }

      int var12;
      int var13;
      while(var11 < this.mSpanCount && var3.hasMore(var2) && var7 > 0) {
         var13 = var3.mCurrentPosition;
         var12 = this.getSpanSize(var1, var2, var13);
         if (var12 > this.mSpanCount) {
            StringBuilder var17 = new StringBuilder();
            var17.append("Item at position ");
            var17.append(var13);
            var17.append(" requires ");
            var17.append(var12);
            var17.append(" spans but GridLayoutManager has only ");
            var17.append(this.mSpanCount);
            var17.append(" spans.");
            throw new IllegalArgumentException(var17.toString());
         }

         var7 -= var12;
         if (var7 < 0) {
            break;
         }

         View var16 = var3.next(var1);
         if (var16 == null) {
            break;
         }

         var8 += var12;
         this.mSet[var11] = var16;
         ++var11;
      }

      if (var11 == 0) {
         var4.mFinished = true;
      } else {
         this.assignSpans(var1, var2, var11, var8, var15);
         var8 = 0;
         var7 = 0;

         float var5;
         View var18;
         GridLayoutManager.LayoutParams var19;
         for(var5 = 0.0F; var8 < var11; ++var8) {
            var18 = this.mSet[var8];
            if (var3.mScrapList == null) {
               if (var15) {
                  this.addView(var18);
               } else {
                  this.addView(var18, 0);
               }
            } else if (var15) {
               this.addDisappearingView(var18);
            } else {
               this.addDisappearingView(var18, 0);
            }

            this.calculateItemDecorationsForChild(var18, this.mDecorInsets);
            this.measureChild(var18, var14, false);
            var12 = this.mOrientationHelper.getDecoratedMeasurement(var18);
            if (var12 > var7) {
               var7 = var12;
            }

            var19 = (GridLayoutManager.LayoutParams)var18.getLayoutParams();
            float var6 = (float)this.mOrientationHelper.getDecoratedMeasurementInOther(var18) * 1.0F / (float)var19.mSpanSize;
            if (var6 > var5) {
               var5 = var6;
            }
         }

         int var20;
         if (var9) {
            this.guessMeasurement(var5, var10);
            var7 = 0;

            for(var8 = 0; var8 < var11; ++var8) {
               var18 = this.mSet[var8];
               this.measureChild(var18, 1073741824, true);
               var20 = this.mOrientationHelper.getDecoratedMeasurement(var18);
               if (var20 > var7) {
                  var7 = var20;
               }
            }

            var12 = var7;
         } else {
            var12 = var7;
         }

         for(var8 = 0; var8 < var11; ++var8) {
            var18 = this.mSet[var8];
            if (this.mOrientationHelper.getDecoratedMeasurement(var18) != var12) {
               var19 = (GridLayoutManager.LayoutParams)var18.getLayoutParams();
               Rect var21 = var19.mDecorInsets;
               var10 = var21.top + var21.bottom + var19.topMargin + var19.bottomMargin;
               var20 = var21.left + var21.right + var19.leftMargin + var19.rightMargin;
               var14 = this.getSpaceForSpanRange(var19.mSpanIndex, var19.mSpanSize);
               if (this.mOrientation == 1) {
                  var20 = getChildMeasureSpec(var14, 1073741824, var20, var19.width, false);
                  var10 = MeasureSpec.makeMeasureSpec(var12 - var10, 1073741824);
               } else {
                  var20 = MeasureSpec.makeMeasureSpec(var12 - var20, 1073741824);
                  var10 = getChildMeasureSpec(var14, 1073741824, var10, var19.height, false);
               }

               this.measureChildWithDecorationsAndMargin(var18, var20, var10, true);
            }
         }

         var4.mConsumed = var12;
         var7 = 0;
         var20 = 0;
         var10 = 0;
         var8 = 0;
         if (this.mOrientation == 1) {
            if (var3.mLayoutDirection == -1) {
               var8 = var3.mOffset;
               var10 = var8 - var12;
            } else {
               var10 = var3.mOffset;
               var8 = var10 + var12;
            }
         } else if (var3.mLayoutDirection == -1) {
            var20 = var3.mOffset;
            var7 = var20 - var12;
         } else {
            var7 = var3.mOffset;
            var20 = var7 + var12;
         }

         for(var13 = 0; var13 < var11; var13 = var14) {
            var18 = this.mSet[var13];
            var19 = (GridLayoutManager.LayoutParams)var18.getLayoutParams();
            if (this.mOrientation == 1) {
               if (this.isLayoutRTL()) {
                  var20 = this.getPaddingLeft() + this.mCachedBorders[this.mSpanCount - var19.mSpanIndex];
                  var14 = var20 - this.mOrientationHelper.getDecoratedMeasurementInOther(var18);
                  var7 = var10;
                  var10 = var8;
                  var8 = var14;
               } else {
                  var7 = this.getPaddingLeft() + this.mCachedBorders[var19.mSpanIndex];
                  var14 = this.mOrientationHelper.getDecoratedMeasurementInOther(var18);
                  var20 = var7;
                  var14 += var7;
                  var7 = var10;
                  var10 = var8;
                  var8 = var20;
                  var20 = var14;
               }
            } else {
               var8 = this.getPaddingTop() + this.mCachedBorders[var19.mSpanIndex];
               var10 = this.mOrientationHelper.getDecoratedMeasurementInOther(var18);
               var14 = var8;
               var10 += var8;
               var8 = var7;
               var7 = var14;
            }

            this.layoutDecoratedWithMargins(var18, var8, var7, var20, var10);
            if (var19.isItemRemoved() || var19.isItemChanged()) {
               var4.mIgnoreConsumed = true;
            }

            var4.mFocusable |= var18.hasFocusable();
            var14 = var13 + 1;
            var13 = var7;
            var7 = var8;
            var8 = var10;
            var10 = var13;
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
      View var24 = this.findContainingItemView(var1);
      if (var24 == null) {
         return null;
      } else {
         GridLayoutManager.LayoutParams var22 = (GridLayoutManager.LayoutParams)var24.getLayoutParams();
         int var16 = var22.mSpanIndex;
         int var17 = var22.mSpanIndex + var22.mSpanSize;
         if (super.onFocusSearchFailed(var1, var2, var3, var4) == null) {
            return null;
         } else {
            boolean var21;
            if (this.convertFocusDirectionToLayoutDirection(var2) == 1) {
               var21 = true;
            } else {
               var21 = false;
            }

            boolean var26;
            if (var21 != this.mShouldReverseLayout) {
               var26 = true;
            } else {
               var26 = false;
            }

            byte var7;
            int var8;
            if (var26) {
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

            View var23 = null;
            View var28 = null;
            int var11 = this.getSpanGroupIndex(var3, var4, var2);
            int var12 = var2;
            int var5 = -1;
            int var6 = 0;
            int var14 = -1;
            int var13 = 0;

            for(var1 = var24; var12 != var8; var6 = var2) {
               var2 = this.getSpanGroupIndex(var3, var4, var12);
               var24 = this.getChildAt(var12);
               if (var24 == var1) {
                  break;
               }

               label161: {
                  if (var24.hasFocusable() && var2 != var11) {
                     if (var23 != null) {
                        break;
                     }
                  } else {
                     GridLayoutManager.LayoutParams var25 = (GridLayoutManager.LayoutParams)var24.getLayoutParams();
                     int var18 = var25.mSpanIndex;
                     int var19 = var25.mSpanIndex + var25.mSpanSize;
                     if (var24.hasFocusable() && var18 == var16 && var19 == var17) {
                        return var24;
                     }

                     if (var24.hasFocusable() && var23 == null || !var24.hasFocusable() && var28 == null) {
                        var26 = true;
                     } else {
                        var2 = Math.max(var18, var16);
                        int var20 = Math.min(var19, var17);
                        boolean var15 = false;
                        var20 -= var2;
                        if (var24.hasFocusable()) {
                           if (var20 > var6) {
                              var26 = true;
                           } else {
                              label154: {
                                 if (var20 == var6) {
                                    if (var18 > var5) {
                                       var26 = true;
                                    } else {
                                       var26 = false;
                                    }

                                    if (var9 == var26) {
                                       var26 = true;
                                       break label154;
                                    }
                                 }

                                 var26 = var15;
                              }
                           }
                        } else if (var23 == null) {
                           var26 = true;
                           if (this.isViewPartiallyVisible(var24, false, true)) {
                              if (var20 > var13) {
                                 var26 = true;
                              } else {
                                 label155: {
                                    if (var20 == var13) {
                                       if (var18 <= var14) {
                                          var26 = false;
                                       }

                                       if (var9 == var26) {
                                          var26 = true;
                                          break label155;
                                       }
                                    }

                                    var26 = var15;
                                 }
                              }
                           } else {
                              var26 = var15;
                           }
                        } else {
                           var26 = var15;
                        }
                     }

                     int var27 = var6;
                     if (var26) {
                        if (var24.hasFocusable()) {
                           var5 = var25.mSpanIndex;
                           var2 = Math.min(var19, var17) - Math.max(var18, var16);
                           var23 = var24;
                        } else {
                           var14 = var25.mSpanIndex;
                           var2 = Math.min(var19, var17);
                           var6 = Math.max(var18, var16);
                           var28 = var24;
                           var13 = var2 - var6;
                           var2 = var27;
                        }
                        break label161;
                     }
                  }

                  var2 = var6;
               }

               var12 += var7;
            }

            return var23 != null ? var23 : var28;
         }
      }
   }

   public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler var1, RecyclerView.State var2, View var3, AccessibilityNodeInfoCompat var4) {
      android.view.ViewGroup.LayoutParams var9 = var3.getLayoutParams();
      if (!(var9 instanceof GridLayoutManager.LayoutParams)) {
         super.onInitializeAccessibilityNodeInfoForItem(var3, var4);
      } else {
         GridLayoutManager.LayoutParams var10 = (GridLayoutManager.LayoutParams)var9;
         int var5 = this.getSpanGroupIndex(var1, var2, var10.getViewLayoutPosition());
         int var6;
         int var7;
         boolean var8;
         if (this.mOrientation == 0) {
            var6 = var10.getSpanIndex();
            var7 = var10.getSpanSize();
            if (this.mSpanCount > 1 && var10.getSpanSize() == this.mSpanCount) {
               var8 = true;
            } else {
               var8 = false;
            }

            var4.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(var6, var7, var5, 1, var8, false));
         } else {
            var6 = var10.getSpanIndex();
            var7 = var10.getSpanSize();
            if (this.mSpanCount > 1 && var10.getSpanSize() == this.mSpanCount) {
               var8 = true;
            } else {
               var8 = false;
            }

            var4.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(var5, 1, var6, var7, var8, false));
         }
      }
   }

   public void onItemsAdded(RecyclerView var1, int var2, int var3) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
   }

   public void onItemsChanged(RecyclerView var1) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
   }

   public void onItemsMoved(RecyclerView var1, int var2, int var3, int var4) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
   }

   public void onItemsRemoved(RecyclerView var1, int var2, int var3) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
   }

   public void onItemsUpdated(RecyclerView var1, int var2, int var3, Object var4) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
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

      public LayoutParams(RecyclerView.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
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
      private boolean mCacheSpanIndices = false;
      final SparseIntArray mSpanIndexCache = new SparseIntArray();

      int findReferenceIndexFromCache(int var1) {
         int var3 = 0;
         int var2 = this.mSpanIndexCache.size() - 1;

         while(var3 <= var2) {
            int var4 = var3 + var2 >>> 1;
            if (this.mSpanIndexCache.keyAt(var4) < var1) {
               var3 = var4 + 1;
            } else {
               var2 = var4 - 1;
            }
         }

         var1 = var3 - 1;
         if (var1 >= 0 && var1 < this.mSpanIndexCache.size()) {
            return this.mSpanIndexCache.keyAt(var1);
         } else {
            return -1;
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
         int var3 = 0;
         int var4 = 0;
         int var7 = this.getSpanSize(var1);

         for(int var5 = 0; var5 < var1; ++var5) {
            int var6 = this.getSpanSize(var5);
            var3 += var6;
            if (var3 == var2) {
               var3 = 0;
               ++var4;
            } else if (var3 > var2) {
               var3 = var6;
               ++var4;
            }
         }

         if (var3 + var7 > var2) {
            return var4 + 1;
         } else {
            return var4;
         }
      }

      public int getSpanIndex(int var1, int var2) {
         int var6 = this.getSpanSize(var1);
         if (var6 == var2) {
            return 0;
         } else {
            int var3 = 0;
            int var4 = 0;
            int var5;
            if (this.mCacheSpanIndices && this.mSpanIndexCache.size() > 0) {
               var5 = this.findReferenceIndexFromCache(var1);
               if (var5 >= 0) {
                  var3 = this.mSpanIndexCache.get(var5) + this.getSpanSize(var5);
                  var4 = var5 + 1;
               }
            }

            for(; var4 < var1; ++var4) {
               var5 = this.getSpanSize(var4);
               var3 += var5;
               if (var3 == var2) {
                  var3 = 0;
               } else if (var3 > var2) {
                  var3 = var5;
               }
            }

            if (var3 + var6 <= var2) {
               return var3;
            } else {
               return 0;
            }
         }
      }

      public abstract int getSpanSize(int var1);

      public void invalidateSpanIndexCache() {
         this.mSpanIndexCache.clear();
      }

      public boolean isSpanIndexCacheEnabled() {
         return this.mCacheSpanIndices;
      }

      public void setSpanIndexCacheEnabled(boolean var1) {
         this.mCacheSpanIndices = var1;
      }
   }
}
