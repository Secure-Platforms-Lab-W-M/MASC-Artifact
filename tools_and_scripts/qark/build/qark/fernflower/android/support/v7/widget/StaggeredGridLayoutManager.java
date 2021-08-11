package android.support.v7.widget;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class StaggeredGridLayoutManager extends RecyclerView.LayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider {
   static final boolean DEBUG = false;
   @Deprecated
   public static final int GAP_HANDLING_LAZY = 1;
   public static final int GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS = 2;
   public static final int GAP_HANDLING_NONE = 0;
   public static final int HORIZONTAL = 0;
   static final int INVALID_OFFSET = Integer.MIN_VALUE;
   private static final float MAX_SCROLL_FACTOR = 0.33333334F;
   private static final String TAG = "StaggeredGridLManager";
   public static final int VERTICAL = 1;
   private final StaggeredGridLayoutManager.AnchorInfo mAnchorInfo = new StaggeredGridLayoutManager.AnchorInfo();
   private final Runnable mCheckForGapsRunnable;
   private int mFullSizeSpec;
   private int mGapStrategy = 2;
   private boolean mLaidOutInvalidFullSpan = false;
   private boolean mLastLayoutFromEnd;
   private boolean mLastLayoutRTL;
   @NonNull
   private final LayoutState mLayoutState;
   StaggeredGridLayoutManager.LazySpanLookup mLazySpanLookup = new StaggeredGridLayoutManager.LazySpanLookup();
   private int mOrientation;
   private StaggeredGridLayoutManager.SavedState mPendingSavedState;
   int mPendingScrollPosition = -1;
   int mPendingScrollPositionOffset = Integer.MIN_VALUE;
   private int[] mPrefetchDistances;
   @NonNull
   OrientationHelper mPrimaryOrientation;
   private BitSet mRemainingSpans;
   boolean mReverseLayout = false;
   @NonNull
   OrientationHelper mSecondaryOrientation;
   boolean mShouldReverseLayout = false;
   private int mSizePerSpan;
   private boolean mSmoothScrollbarEnabled;
   private int mSpanCount = -1;
   StaggeredGridLayoutManager.Span[] mSpans;
   private final Rect mTmpRect = new Rect();

   public StaggeredGridLayoutManager(int var1, int var2) {
      boolean var3 = true;
      this.mSmoothScrollbarEnabled = true;
      this.mCheckForGapsRunnable = new Runnable() {
         public void run() {
            StaggeredGridLayoutManager.this.checkForGaps();
         }
      };
      this.mOrientation = var2;
      this.setSpanCount(var1);
      if (this.mGapStrategy == 0) {
         var3 = false;
      }

      this.setAutoMeasureEnabled(var3);
      this.mLayoutState = new LayoutState();
      this.createOrientationHelpers();
   }

   public StaggeredGridLayoutManager(Context var1, AttributeSet var2, int var3, int var4) {
      boolean var5 = true;
      this.mSmoothScrollbarEnabled = true;
      this.mCheckForGapsRunnable = new Runnable() {
         public void run() {
            StaggeredGridLayoutManager.this.checkForGaps();
         }
      };
      RecyclerView.LayoutManager.Properties var6 = getProperties(var1, var2, var3, var4);
      this.setOrientation(var6.orientation);
      this.setSpanCount(var6.spanCount);
      this.setReverseLayout(var6.reverseLayout);
      if (this.mGapStrategy == 0) {
         var5 = false;
      }

      this.setAutoMeasureEnabled(var5);
      this.mLayoutState = new LayoutState();
      this.createOrientationHelpers();
   }

   private void appendViewToAllSpans(View var1) {
      for(int var2 = this.mSpanCount - 1; var2 >= 0; --var2) {
         this.mSpans[var2].appendToSpan(var1);
      }

   }

   private void applyPendingSavedState(StaggeredGridLayoutManager.AnchorInfo var1) {
      if (this.mPendingSavedState.mSpanOffsetsSize > 0) {
         if (this.mPendingSavedState.mSpanOffsetsSize == this.mSpanCount) {
            for(int var3 = 0; var3 < this.mSpanCount; ++var3) {
               this.mSpans[var3].clear();
               int var2 = this.mPendingSavedState.mSpanOffsets[var3];
               if (var2 != Integer.MIN_VALUE) {
                  if (this.mPendingSavedState.mAnchorLayoutFromEnd) {
                     var2 += this.mPrimaryOrientation.getEndAfterPadding();
                  } else {
                     var2 += this.mPrimaryOrientation.getStartAfterPadding();
                  }
               }

               this.mSpans[var3].setLine(var2);
            }
         } else {
            this.mPendingSavedState.invalidateSpanInfo();
            StaggeredGridLayoutManager.SavedState var4 = this.mPendingSavedState;
            var4.mAnchorPosition = var4.mVisibleAnchorPosition;
         }
      }

      this.mLastLayoutRTL = this.mPendingSavedState.mLastLayoutRTL;
      this.setReverseLayout(this.mPendingSavedState.mReverseLayout);
      this.resolveShouldLayoutReverse();
      if (this.mPendingSavedState.mAnchorPosition != -1) {
         this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
         var1.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
      } else {
         var1.mLayoutFromEnd = this.mShouldReverseLayout;
      }

      if (this.mPendingSavedState.mSpanLookupSize > 1) {
         this.mLazySpanLookup.mData = this.mPendingSavedState.mSpanLookup;
         this.mLazySpanLookup.mFullSpanItems = this.mPendingSavedState.mFullSpanItems;
      }
   }

   private void attachViewToSpans(View var1, StaggeredGridLayoutManager.LayoutParams var2, LayoutState var3) {
      if (var3.mLayoutDirection == 1) {
         if (var2.mFullSpan) {
            this.appendViewToAllSpans(var1);
         } else {
            var2.mSpan.appendToSpan(var1);
         }
      } else if (var2.mFullSpan) {
         this.prependViewToAllSpans(var1);
      } else {
         var2.mSpan.prependToSpan(var1);
      }
   }

   private int calculateScrollDirectionForPosition(int var1) {
      int var3 = this.getChildCount();
      byte var2 = -1;
      if (var3 == 0) {
         byte var5 = var2;
         if (this.mShouldReverseLayout) {
            var5 = 1;
         }

         return var5;
      } else {
         boolean var4;
         if (var1 < this.getFirstChildPosition()) {
            var4 = true;
         } else {
            var4 = false;
         }

         return var4 != this.mShouldReverseLayout ? -1 : 1;
      }
   }

   private boolean checkSpanForGap(StaggeredGridLayoutManager.Span var1) {
      if (this.mShouldReverseLayout) {
         return var1.getEndLine() < this.mPrimaryOrientation.getEndAfterPadding() ? var1.getLayoutParams((View)var1.mViews.get(var1.mViews.size() - 1)).mFullSpan ^ true : false;
      } else {
         return var1.getStartLine() > this.mPrimaryOrientation.getStartAfterPadding() ? var1.getLayoutParams((View)var1.mViews.get(0)).mFullSpan ^ true : false;
      }
   }

   private int computeScrollExtent(RecyclerView.State var1) {
      return this.getChildCount() == 0 ? 0 : ScrollbarHelper.computeScrollExtent(var1, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled);
   }

   private int computeScrollOffset(RecyclerView.State var1) {
      return this.getChildCount() == 0 ? 0 : ScrollbarHelper.computeScrollOffset(var1, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
   }

   private int computeScrollRange(RecyclerView.State var1) {
      return this.getChildCount() == 0 ? 0 : ScrollbarHelper.computeScrollRange(var1, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled);
   }

   private int convertFocusDirectionToLayoutDirection(int var1) {
      int var2 = Integer.MIN_VALUE;
      if (var1 != 17) {
         if (var1 != 33) {
            if (var1 != 66) {
               if (var1 != 130) {
                  switch(var1) {
                  case 1:
                     if (this.mOrientation == 1) {
                        return -1;
                     } else {
                        if (this.isLayoutRTL()) {
                           return 1;
                        }

                        return -1;
                     }
                  case 2:
                     if (this.mOrientation == 1) {
                        return 1;
                     } else {
                        if (this.isLayoutRTL()) {
                           return -1;
                        }

                        return 1;
                     }
                  default:
                     return Integer.MIN_VALUE;
                  }
               } else {
                  if (this.mOrientation == 1) {
                     var2 = 1;
                  }

                  return var2;
               }
            } else {
               if (this.mOrientation == 0) {
                  var2 = 1;
               }

               return var2;
            }
         } else {
            return this.mOrientation == 1 ? -1 : Integer.MIN_VALUE;
         }
      } else {
         return this.mOrientation == 0 ? -1 : Integer.MIN_VALUE;
      }
   }

   private StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFullSpanItemFromEnd(int var1) {
      StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var3 = new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem();
      var3.mGapPerSpan = new int[this.mSpanCount];

      for(int var2 = 0; var2 < this.mSpanCount; ++var2) {
         var3.mGapPerSpan[var2] = var1 - this.mSpans[var2].getEndLine(var1);
      }

      return var3;
   }

   private StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFullSpanItemFromStart(int var1) {
      StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var3 = new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem();
      var3.mGapPerSpan = new int[this.mSpanCount];

      for(int var2 = 0; var2 < this.mSpanCount; ++var2) {
         var3.mGapPerSpan[var2] = this.mSpans[var2].getStartLine(var1) - var1;
      }

      return var3;
   }

   private void createOrientationHelpers() {
      this.mPrimaryOrientation = OrientationHelper.createOrientationHelper(this, this.mOrientation);
      this.mSecondaryOrientation = OrientationHelper.createOrientationHelper(this, 1 - this.mOrientation);
   }

   private int fill(RecyclerView.Recycler var1, LayoutState var2, RecyclerView.State var3) {
      this.mRemainingSpans.set(0, this.mSpanCount, true);
      int var4;
      if (this.mLayoutState.mInfinite) {
         if (var2.mLayoutDirection == 1) {
            var4 = Integer.MAX_VALUE;
         } else {
            var4 = Integer.MIN_VALUE;
         }
      } else if (var2.mLayoutDirection == 1) {
         var4 = var2.mEndLine + var2.mAvailable;
      } else {
         var4 = var2.mStartLine - var2.mAvailable;
      }

      this.updateAllRemainingSpans(var2.mLayoutDirection, var4);
      int var6;
      if (this.mShouldReverseLayout) {
         var6 = this.mPrimaryOrientation.getEndAfterPadding();
      } else {
         var6 = this.mPrimaryOrientation.getStartAfterPadding();
      }

      boolean var5;
      int var15;
      for(var5 = false; var2.hasMore(var3) && (this.mLayoutState.mInfinite || !this.mRemainingSpans.isEmpty()); var5 = true) {
         View var12 = var2.next(var1);
         StaggeredGridLayoutManager.LayoutParams var13 = (StaggeredGridLayoutManager.LayoutParams)var12.getLayoutParams();
         int var10 = var13.getViewLayoutPosition();
         var15 = this.mLazySpanLookup.getSpan(var10);
         boolean var9;
         if (var15 == -1) {
            var9 = true;
         } else {
            var9 = false;
         }

         StaggeredGridLayoutManager.Span var11;
         if (var9) {
            if (var13.mFullSpan) {
               var11 = this.mSpans[0];
            } else {
               var11 = this.getNextSpan(var2);
            }

            this.mLazySpanLookup.setSpan(var10, var11);
         } else {
            var11 = this.mSpans[var15];
         }

         var13.mSpan = var11;
         if (var2.mLayoutDirection == 1) {
            this.addView(var12);
         } else {
            this.addView(var12, 0);
         }

         this.measureChildWithDecorationsAndMargin(var12, var13, false);
         int var7;
         int var8;
         StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var14;
         if (var2.mLayoutDirection == 1) {
            if (var13.mFullSpan) {
               var15 = this.getMaxEnd(var6);
            } else {
               var15 = var11.getEndLine(var6);
            }

            var8 = this.mPrimaryOrientation.getDecoratedMeasurement(var12);
            if (var9 && var13.mFullSpan) {
               var14 = this.createFullSpanItemFromEnd(var15);
               var14.mGapDir = -1;
               var14.mPosition = var10;
               this.mLazySpanLookup.addFullSpanItem(var14);
            }

            var7 = var15;
            var8 += var15;
         } else {
            if (var13.mFullSpan) {
               var15 = this.getMinStart(var6);
            } else {
               var15 = var11.getStartLine(var6);
            }

            var7 = this.mPrimaryOrientation.getDecoratedMeasurement(var12);
            if (var9 && var13.mFullSpan) {
               var14 = this.createFullSpanItemFromStart(var15);
               var14.mGapDir = 1;
               var14.mPosition = var10;
               this.mLazySpanLookup.addFullSpanItem(var14);
            }

            var8 = var15;
            var7 = var15 - var7;
         }

         if (var13.mFullSpan && var2.mItemDirection == -1) {
            if (var9) {
               this.mLaidOutInvalidFullSpan = true;
            } else {
               if (var2.mLayoutDirection == 1) {
                  var5 = this.areAllEndsEqual() ^ true;
               } else {
                  var5 = this.areAllStartsEqual() ^ true;
               }

               if (var5) {
                  var14 = this.mLazySpanLookup.getFullSpanItem(var10);
                  if (var14 != null) {
                     var14.mHasUnwantedGapAfter = true;
                  }

                  this.mLaidOutInvalidFullSpan = true;
               }
            }
         }

         this.attachViewToSpans(var12, var13, var2);
         if (this.isLayoutRTL() && this.mOrientation == 1) {
            if (var13.mFullSpan) {
               var15 = this.mSecondaryOrientation.getEndAfterPadding();
            } else {
               var15 = this.mSecondaryOrientation.getEndAfterPadding() - (this.mSpanCount - 1 - var11.mIndex) * this.mSizePerSpan;
            }

            var10 = this.mSecondaryOrientation.getDecoratedMeasurement(var12);
            int var17 = var15;
            var15 -= var10;
            var10 = var17;
         } else {
            if (var13.mFullSpan) {
               var15 = this.mSecondaryOrientation.getStartAfterPadding();
            } else {
               var15 = var11.mIndex * this.mSizePerSpan + this.mSecondaryOrientation.getStartAfterPadding();
            }

            var10 = this.mSecondaryOrientation.getDecoratedMeasurement(var12);
            var10 += var15;
            var15 = var15;
         }

         if (this.mOrientation == 1) {
            this.layoutDecoratedWithMargins(var12, var15, var7, var10, var8);
         } else {
            this.layoutDecoratedWithMargins(var12, var7, var15, var8, var10);
         }

         if (var13.mFullSpan) {
            this.updateAllRemainingSpans(this.mLayoutState.mLayoutDirection, var4);
         } else {
            this.updateRemainingSpans(var11, this.mLayoutState.mLayoutDirection, var4);
         }

         this.recycle(var1, this.mLayoutState);
         if (this.mLayoutState.mStopInFocusable && var12.hasFocusable()) {
            if (var13.mFullSpan) {
               this.mRemainingSpans.clear();
            } else {
               this.mRemainingSpans.set(var11.mIndex, false);
            }
         }
      }

      byte var16 = 0;
      if (!var5) {
         this.recycle(var1, this.mLayoutState);
      }

      if (this.mLayoutState.mLayoutDirection == -1) {
         var4 = this.getMinStart(this.mPrimaryOrientation.getStartAfterPadding());
         var4 = this.mPrimaryOrientation.getStartAfterPadding() - var4;
      } else {
         var4 = this.getMaxEnd(this.mPrimaryOrientation.getEndAfterPadding()) - this.mPrimaryOrientation.getEndAfterPadding();
      }

      var15 = var16;
      if (var4 > 0) {
         var15 = Math.min(var2.mAvailable, var4);
      }

      return var15;
   }

   private int findFirstReferenceChildPosition(int var1) {
      int var3 = this.getChildCount();

      for(int var2 = 0; var2 < var3; ++var2) {
         int var4 = this.getPosition(this.getChildAt(var2));
         if (var4 >= 0 && var4 < var1) {
            return var4;
         }
      }

      return 0;
   }

   private int findLastReferenceChildPosition(int var1) {
      for(int var2 = this.getChildCount() - 1; var2 >= 0; --var2) {
         int var3 = this.getPosition(this.getChildAt(var2));
         if (var3 >= 0 && var3 < var1) {
            return var3;
         }
      }

      return 0;
   }

   private void fixEndGap(RecyclerView.Recycler var1, RecyclerView.State var2, boolean var3) {
      int var4 = this.getMaxEnd(Integer.MIN_VALUE);
      if (var4 != Integer.MIN_VALUE) {
         var4 = this.mPrimaryOrientation.getEndAfterPadding() - var4;
         if (var4 > 0) {
            var4 -= -this.scrollBy(-var4, var1, var2);
            if (var3 && var4 > 0) {
               this.mPrimaryOrientation.offsetChildren(var4);
            }
         }
      }
   }

   private void fixStartGap(RecyclerView.Recycler var1, RecyclerView.State var2, boolean var3) {
      int var4 = this.getMinStart(Integer.MAX_VALUE);
      if (var4 != Integer.MAX_VALUE) {
         var4 -= this.mPrimaryOrientation.getStartAfterPadding();
         if (var4 > 0) {
            var4 -= this.scrollBy(var4, var1, var2);
            if (var3 && var4 > 0) {
               this.mPrimaryOrientation.offsetChildren(-var4);
            }
         }
      }
   }

   private int getMaxEnd(int var1) {
      int var3 = this.mSpans[0].getEndLine(var1);

      for(int var2 = 1; var2 < this.mSpanCount; ++var2) {
         int var4 = this.mSpans[var2].getEndLine(var1);
         if (var4 > var3) {
            var3 = var4;
         }
      }

      return var3;
   }

   private int getMaxStart(int var1) {
      int var3 = this.mSpans[0].getStartLine(var1);

      for(int var2 = 1; var2 < this.mSpanCount; ++var2) {
         int var4 = this.mSpans[var2].getStartLine(var1);
         if (var4 > var3) {
            var3 = var4;
         }
      }

      return var3;
   }

   private int getMinEnd(int var1) {
      int var3 = this.mSpans[0].getEndLine(var1);

      for(int var2 = 1; var2 < this.mSpanCount; ++var2) {
         int var4 = this.mSpans[var2].getEndLine(var1);
         if (var4 < var3) {
            var3 = var4;
         }
      }

      return var3;
   }

   private int getMinStart(int var1) {
      int var3 = this.mSpans[0].getStartLine(var1);

      for(int var2 = 1; var2 < this.mSpanCount; ++var2) {
         int var4 = this.mSpans[var2].getStartLine(var1);
         if (var4 < var3) {
            var3 = var4;
         }
      }

      return var3;
   }

   private StaggeredGridLayoutManager.Span getNextSpan(LayoutState var1) {
      int var2;
      int var3;
      byte var4;
      if (this.preferLastSpan(var1.mLayoutDirection)) {
         var2 = this.mSpanCount - 1;
         var3 = -1;
         var4 = -1;
      } else {
         var2 = 0;
         var3 = this.mSpanCount;
         var4 = 1;
      }

      int var5;
      int var6;
      int var7;
      StaggeredGridLayoutManager.Span var8;
      StaggeredGridLayoutManager.Span var9;
      if (var1.mLayoutDirection == 1) {
         var9 = null;
         var5 = Integer.MAX_VALUE;

         for(var7 = this.mPrimaryOrientation.getStartAfterPadding(); var2 != var3; var2 += var4) {
            var8 = this.mSpans[var2];
            var6 = var8.getEndLine(var7);
            if (var6 < var5) {
               var9 = var8;
               var5 = var6;
            }
         }

         return var9;
      } else {
         var9 = null;
         var5 = Integer.MIN_VALUE;

         for(var7 = this.mPrimaryOrientation.getEndAfterPadding(); var2 != var3; var2 += var4) {
            var8 = this.mSpans[var2];
            var6 = var8.getStartLine(var7);
            if (var6 > var5) {
               var9 = var8;
               var5 = var6;
            }
         }

         return var9;
      }
   }

   private void handleUpdate(int var1, int var2, int var3) {
      int var6;
      if (this.mShouldReverseLayout) {
         var6 = this.getLastChildPosition();
      } else {
         var6 = this.getFirstChildPosition();
      }

      int var4;
      int var5;
      if (var3 == 8) {
         if (var1 < var2) {
            var5 = var2 + 1;
            var4 = var1;
         } else {
            var5 = var1 + 1;
            var4 = var2;
         }
      } else {
         var4 = var1;
         var5 = var1 + var2;
      }

      this.mLazySpanLookup.invalidateAfter(var4);
      if (var3 != 8) {
         switch(var3) {
         case 1:
            this.mLazySpanLookup.offsetForAddition(var1, var2);
            break;
         case 2:
            this.mLazySpanLookup.offsetForRemoval(var1, var2);
         }
      } else {
         this.mLazySpanLookup.offsetForRemoval(var1, 1);
         this.mLazySpanLookup.offsetForAddition(var2, 1);
      }

      if (var5 > var6) {
         if (this.mShouldReverseLayout) {
            var1 = this.getFirstChildPosition();
         } else {
            var1 = this.getLastChildPosition();
         }

         if (var4 <= var1) {
            this.requestLayout();
         }
      }
   }

   private void measureChildWithDecorationsAndMargin(View var1, int var2, int var3, boolean var4) {
      this.calculateItemDecorationsForChild(var1, this.mTmpRect);
      StaggeredGridLayoutManager.LayoutParams var5 = (StaggeredGridLayoutManager.LayoutParams)var1.getLayoutParams();
      var2 = this.updateSpecWithExtra(var2, var5.leftMargin + this.mTmpRect.left, var5.rightMargin + this.mTmpRect.right);
      var3 = this.updateSpecWithExtra(var3, var5.topMargin + this.mTmpRect.top, var5.bottomMargin + this.mTmpRect.bottom);
      if (var4) {
         var4 = this.shouldReMeasureChild(var1, var2, var3, var5);
      } else {
         var4 = this.shouldMeasureChild(var1, var2, var3, var5);
      }

      if (var4) {
         var1.measure(var2, var3);
      }
   }

   private void measureChildWithDecorationsAndMargin(View var1, StaggeredGridLayoutManager.LayoutParams var2, boolean var3) {
      if (var2.mFullSpan) {
         if (this.mOrientation == 1) {
            this.measureChildWithDecorationsAndMargin(var1, this.mFullSizeSpec, getChildMeasureSpec(this.getHeight(), this.getHeightMode(), 0, var2.height, true), var3);
         } else {
            this.measureChildWithDecorationsAndMargin(var1, getChildMeasureSpec(this.getWidth(), this.getWidthMode(), 0, var2.width, true), this.mFullSizeSpec, var3);
         }
      } else if (this.mOrientation == 1) {
         this.measureChildWithDecorationsAndMargin(var1, getChildMeasureSpec(this.mSizePerSpan, this.getWidthMode(), 0, var2.width, false), getChildMeasureSpec(this.getHeight(), this.getHeightMode(), 0, var2.height, true), var3);
      } else {
         this.measureChildWithDecorationsAndMargin(var1, getChildMeasureSpec(this.getWidth(), this.getWidthMode(), 0, var2.width, true), getChildMeasureSpec(this.mSizePerSpan, this.getHeightMode(), 0, var2.height, false), var3);
      }
   }

   private void onLayoutChildren(RecyclerView.Recycler var1, RecyclerView.State var2, boolean var3) {
      StaggeredGridLayoutManager.AnchorInfo var8 = this.mAnchorInfo;
      if ((this.mPendingSavedState != null || this.mPendingScrollPosition != -1) && var2.getItemCount() == 0) {
         this.removeAndRecycleAllViews(var1);
         var8.reset();
      } else {
         boolean var7 = var8.mValid;
         boolean var5 = true;
         boolean var4;
         if (var7 && this.mPendingScrollPosition == -1 && this.mPendingSavedState == null) {
            var4 = false;
         } else {
            var4 = true;
         }

         if (var4) {
            var8.reset();
            if (this.mPendingSavedState != null) {
               this.applyPendingSavedState(var8);
            } else {
               this.resolveShouldLayoutReverse();
               var8.mLayoutFromEnd = this.mShouldReverseLayout;
            }

            this.updateAnchorInfoForLayout(var2, var8);
            var8.mValid = true;
         }

         if (this.mPendingSavedState == null && this.mPendingScrollPosition == -1 && (var8.mLayoutFromEnd != this.mLastLayoutFromEnd || this.isLayoutRTL() != this.mLastLayoutRTL)) {
            this.mLazySpanLookup.clear();
            var8.mInvalidateOffsets = true;
         }

         if (this.getChildCount() > 0) {
            StaggeredGridLayoutManager.SavedState var9 = this.mPendingSavedState;
            if (var9 == null || var9.mSpanOffsetsSize < 1) {
               int var10;
               if (var8.mInvalidateOffsets) {
                  for(var10 = 0; var10 < this.mSpanCount; ++var10) {
                     this.mSpans[var10].clear();
                     if (var8.mOffset != Integer.MIN_VALUE) {
                        this.mSpans[var10].setLine(var8.mOffset);
                     }
                  }
               } else if (!var4 && this.mAnchorInfo.mSpanReferenceLines != null) {
                  for(var10 = 0; var10 < this.mSpanCount; ++var10) {
                     StaggeredGridLayoutManager.Span var11 = this.mSpans[var10];
                     var11.clear();
                     var11.setLine(this.mAnchorInfo.mSpanReferenceLines[var10]);
                  }
               } else {
                  for(var10 = 0; var10 < this.mSpanCount; ++var10) {
                     this.mSpans[var10].cacheReferenceLineAndClear(this.mShouldReverseLayout, var8.mOffset);
                  }

                  this.mAnchorInfo.saveSpanReferenceLines(this.mSpans);
               }
            }
         }

         this.detachAndScrapAttachedViews(var1);
         this.mLayoutState.mRecycle = false;
         this.mLaidOutInvalidFullSpan = false;
         this.updateMeasureSpecs(this.mSecondaryOrientation.getTotalSpace());
         this.updateLayoutState(var8.mPosition, var2);
         if (var8.mLayoutFromEnd) {
            this.setLayoutStateDirection(-1);
            this.fill(var1, this.mLayoutState, var2);
            this.setLayoutStateDirection(1);
            this.mLayoutState.mCurrentPosition = var8.mPosition + this.mLayoutState.mItemDirection;
            this.fill(var1, this.mLayoutState, var2);
         } else {
            this.setLayoutStateDirection(1);
            this.fill(var1, this.mLayoutState, var2);
            this.setLayoutStateDirection(-1);
            this.mLayoutState.mCurrentPosition = var8.mPosition + this.mLayoutState.mItemDirection;
            this.fill(var1, this.mLayoutState, var2);
         }

         this.repositionToWrapContentIfNecessary();
         if (this.getChildCount() > 0) {
            if (this.mShouldReverseLayout) {
               this.fixEndGap(var1, var2, true);
               this.fixStartGap(var1, var2, false);
            } else {
               this.fixStartGap(var1, var2, true);
               this.fixEndGap(var1, var2, false);
            }
         }

         boolean var6 = false;
         if (var3 && !var2.isPreLayout()) {
            if (this.mGapStrategy == 0 || this.getChildCount() <= 0 || !this.mLaidOutInvalidFullSpan && this.hasGapsToFix() == null) {
               var4 = false;
            } else {
               var4 = var5;
            }

            if (var4) {
               this.removeCallbacks(this.mCheckForGapsRunnable);
               if (this.checkForGaps()) {
                  var4 = true;
               } else {
                  var4 = var6;
               }
            } else {
               var4 = var6;
            }
         } else {
            var4 = var6;
         }

         if (var2.isPreLayout()) {
            this.mAnchorInfo.reset();
         }

         this.mLastLayoutFromEnd = var8.mLayoutFromEnd;
         this.mLastLayoutRTL = this.isLayoutRTL();
         if (var4) {
            this.mAnchorInfo.reset();
            this.onLayoutChildren(var1, var2, false);
         }
      }
   }

   private boolean preferLastSpan(int var1) {
      boolean var2;
      if (this.mOrientation == 0) {
         if (var1 == -1) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2 != this.mShouldReverseLayout;
      } else {
         if (var1 == -1) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var2 == this.mShouldReverseLayout) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2 == this.isLayoutRTL();
      }
   }

   private void prependViewToAllSpans(View var1) {
      for(int var2 = this.mSpanCount - 1; var2 >= 0; --var2) {
         this.mSpans[var2].prependToSpan(var1);
      }

   }

   private void recycle(RecyclerView.Recycler var1, LayoutState var2) {
      if (var2.mRecycle) {
         if (!var2.mInfinite) {
            if (var2.mAvailable == 0) {
               if (var2.mLayoutDirection == -1) {
                  this.recycleFromEnd(var1, var2.mEndLine);
               } else {
                  this.recycleFromStart(var1, var2.mStartLine);
               }
            } else {
               int var3;
               if (var2.mLayoutDirection == -1) {
                  var3 = var2.mStartLine - this.getMaxStart(var2.mStartLine);
                  if (var3 < 0) {
                     var3 = var2.mEndLine;
                  } else {
                     var3 = var2.mEndLine - Math.min(var3, var2.mAvailable);
                  }

                  this.recycleFromEnd(var1, var3);
               } else {
                  var3 = this.getMinEnd(var2.mEndLine) - var2.mEndLine;
                  if (var3 < 0) {
                     var3 = var2.mStartLine;
                  } else {
                     var3 = var2.mStartLine + Math.min(var3, var2.mAvailable);
                  }

                  this.recycleFromStart(var1, var3);
               }
            }
         }
      }
   }

   private void recycleFromEnd(RecyclerView.Recycler var1, int var2) {
      for(int var3 = this.getChildCount() - 1; var3 >= 0; --var3) {
         View var5 = this.getChildAt(var3);
         if (this.mPrimaryOrientation.getDecoratedStart(var5) < var2) {
            return;
         }

         if (this.mPrimaryOrientation.getTransformedStartWithDecoration(var5) < var2) {
            return;
         }

         StaggeredGridLayoutManager.LayoutParams var6 = (StaggeredGridLayoutManager.LayoutParams)var5.getLayoutParams();
         if (!var6.mFullSpan) {
            if (var6.mSpan.mViews.size() == 1) {
               return;
            }

            var6.mSpan.popEnd();
         } else {
            int var4;
            for(var4 = 0; var4 < this.mSpanCount; ++var4) {
               if (this.mSpans[var4].mViews.size() == 1) {
                  return;
               }
            }

            for(var4 = 0; var4 < this.mSpanCount; ++var4) {
               this.mSpans[var4].popEnd();
            }
         }

         this.removeAndRecycleView(var5, var1);
      }

   }

   private void recycleFromStart(RecyclerView.Recycler var1, int var2) {
      View var4;
      for(; this.getChildCount() > 0; this.removeAndRecycleView(var4, var1)) {
         var4 = this.getChildAt(0);
         if (this.mPrimaryOrientation.getDecoratedEnd(var4) > var2) {
            return;
         }

         if (this.mPrimaryOrientation.getTransformedEndWithDecoration(var4) > var2) {
            return;
         }

         StaggeredGridLayoutManager.LayoutParams var5 = (StaggeredGridLayoutManager.LayoutParams)var4.getLayoutParams();
         if (var5.mFullSpan) {
            int var3;
            for(var3 = 0; var3 < this.mSpanCount; ++var3) {
               if (this.mSpans[var3].mViews.size() == 1) {
                  return;
               }
            }

            for(var3 = 0; var3 < this.mSpanCount; ++var3) {
               this.mSpans[var3].popStart();
            }
         } else {
            if (var5.mSpan.mViews.size() == 1) {
               return;
            }

            var5.mSpan.popStart();
         }
      }

   }

   private void repositionToWrapContentIfNecessary() {
      if (this.mSecondaryOrientation.getMode() != 1073741824) {
         float var1 = 0.0F;
         int var4 = this.getChildCount();

         int var3;
         View var8;
         for(var3 = 0; var3 < var4; ++var3) {
            var8 = this.getChildAt(var3);
            float var2 = (float)this.mSecondaryOrientation.getDecoratedMeasurement(var8);
            if (var2 >= var1) {
               if (((StaggeredGridLayoutManager.LayoutParams)var8.getLayoutParams()).isFullSpan()) {
                  var2 = 1.0F * var2 / (float)this.mSpanCount;
               }

               var1 = Math.max(var1, var2);
            }
         }

         int var5 = this.mSizePerSpan;
         var3 = Math.round((float)this.mSpanCount * var1);
         if (this.mSecondaryOrientation.getMode() == Integer.MIN_VALUE) {
            var3 = Math.min(var3, this.mSecondaryOrientation.getTotalSpace());
         }

         this.updateMeasureSpecs(var3);
         if (this.mSizePerSpan != var5) {
            for(var3 = 0; var3 < var4; ++var3) {
               var8 = this.getChildAt(var3);
               StaggeredGridLayoutManager.LayoutParams var9 = (StaggeredGridLayoutManager.LayoutParams)var8.getLayoutParams();
               if (!var9.mFullSpan) {
                  if (this.isLayoutRTL() && this.mOrientation == 1) {
                     var8.offsetLeftAndRight(-(this.mSpanCount - 1 - var9.mSpan.mIndex) * this.mSizePerSpan - -(this.mSpanCount - 1 - var9.mSpan.mIndex) * var5);
                  } else {
                     int var6 = var9.mSpan.mIndex * this.mSizePerSpan;
                     int var7 = var9.mSpan.mIndex * var5;
                     if (this.mOrientation == 1) {
                        var8.offsetLeftAndRight(var6 - var7);
                     } else {
                        var8.offsetTopAndBottom(var6 - var7);
                     }
                  }
               }
            }

         }
      }
   }

   private void resolveShouldLayoutReverse() {
      if (this.mOrientation != 1 && this.isLayoutRTL()) {
         this.mShouldReverseLayout = this.mReverseLayout ^ true;
      } else {
         this.mShouldReverseLayout = this.mReverseLayout;
      }
   }

   private void setLayoutStateDirection(int var1) {
      LayoutState var5 = this.mLayoutState;
      var5.mLayoutDirection = var1;
      boolean var4 = this.mShouldReverseLayout;
      byte var2 = 1;
      boolean var3;
      if (var1 == -1) {
         var3 = true;
      } else {
         var3 = false;
      }

      byte var6;
      if (var4 == var3) {
         var6 = var2;
      } else {
         var6 = -1;
      }

      var5.mItemDirection = var6;
   }

   private void updateAllRemainingSpans(int var1, int var2) {
      for(int var3 = 0; var3 < this.mSpanCount; ++var3) {
         if (!this.mSpans[var3].mViews.isEmpty()) {
            this.updateRemainingSpans(this.mSpans[var3], var1, var2);
         }
      }

   }

   private boolean updateAnchorFromChildren(RecyclerView.State var1, StaggeredGridLayoutManager.AnchorInfo var2) {
      int var3;
      if (this.mLastLayoutFromEnd) {
         var3 = this.findLastReferenceChildPosition(var1.getItemCount());
      } else {
         var3 = this.findFirstReferenceChildPosition(var1.getItemCount());
      }

      var2.mPosition = var3;
      var2.mOffset = Integer.MIN_VALUE;
      return true;
   }

   private void updateLayoutState(int var1, RecyclerView.State var2) {
      LayoutState var9 = this.mLayoutState;
      boolean var7 = false;
      var9.mAvailable = 0;
      var9.mCurrentPosition = var1;
      byte var4 = 0;
      int var3 = 0;
      boolean var6;
      if (this.isSmoothScrolling()) {
         int var5 = var2.getTargetScrollPosition();
         if (var5 != -1) {
            boolean var8 = this.mShouldReverseLayout;
            if (var5 < var1) {
               var6 = true;
            } else {
               var6 = false;
            }

            if (var8 == var6) {
               var3 = this.mPrimaryOrientation.getTotalSpace();
               var1 = var4;
            } else {
               var1 = this.mPrimaryOrientation.getTotalSpace();
            }
         } else {
            var1 = var4;
         }
      } else {
         var1 = var4;
      }

      if (this.getClipToPadding()) {
         this.mLayoutState.mStartLine = this.mPrimaryOrientation.getStartAfterPadding() - var1;
         this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEndAfterPadding() + var3;
      } else {
         this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEnd() + var3;
         this.mLayoutState.mStartLine = -var1;
      }

      LayoutState var10 = this.mLayoutState;
      var10.mStopInFocusable = false;
      var10.mRecycle = true;
      if (this.mPrimaryOrientation.getMode() == 0 && this.mPrimaryOrientation.getEnd() == 0) {
         var6 = true;
      } else {
         var6 = var7;
      }

      var10.mInfinite = var6;
   }

   private void updateRemainingSpans(StaggeredGridLayoutManager.Span var1, int var2, int var3) {
      int var4 = var1.getDeletedSize();
      if (var2 == -1) {
         if (var1.getStartLine() + var4 <= var3) {
            this.mRemainingSpans.set(var1.mIndex, false);
         }

      } else if (var1.getEndLine() - var4 >= var3) {
         this.mRemainingSpans.set(var1.mIndex, false);
      }
   }

   private int updateSpecWithExtra(int var1, int var2, int var3) {
      if (var2 == 0 && var3 == 0) {
         return var1;
      } else {
         int var4 = MeasureSpec.getMode(var1);
         return var4 != Integer.MIN_VALUE && var4 != 1073741824 ? var1 : MeasureSpec.makeMeasureSpec(Math.max(0, MeasureSpec.getSize(var1) - var2 - var3), var4);
      }
   }

   boolean areAllEndsEqual() {
      int var2 = this.mSpans[0].getEndLine(Integer.MIN_VALUE);

      for(int var1 = 1; var1 < this.mSpanCount; ++var1) {
         if (this.mSpans[var1].getEndLine(Integer.MIN_VALUE) != var2) {
            return false;
         }
      }

      return true;
   }

   boolean areAllStartsEqual() {
      int var2 = this.mSpans[0].getStartLine(Integer.MIN_VALUE);

      for(int var1 = 1; var1 < this.mSpanCount; ++var1) {
         if (this.mSpans[var1].getStartLine(Integer.MIN_VALUE) != var2) {
            return false;
         }
      }

      return true;
   }

   public void assertNotInLayoutOrScroll(String var1) {
      if (this.mPendingSavedState == null) {
         super.assertNotInLayoutOrScroll(var1);
      }
   }

   public boolean canScrollHorizontally() {
      return this.mOrientation == 0;
   }

   public boolean canScrollVertically() {
      return this.mOrientation == 1;
   }

   boolean checkForGaps() {
      if (this.getChildCount() != 0 && this.mGapStrategy != 0) {
         if (!this.isAttachedToWindow()) {
            return false;
         } else {
            int var1;
            int var2;
            if (this.mShouldReverseLayout) {
               var1 = this.getLastChildPosition();
               var2 = this.getFirstChildPosition();
            } else {
               var1 = this.getFirstChildPosition();
               var2 = this.getLastChildPosition();
            }

            if (var1 == 0 && this.hasGapsToFix() != null) {
               this.mLazySpanLookup.clear();
               this.requestSimpleAnimationsInNextLayout();
               this.requestLayout();
               return true;
            } else if (!this.mLaidOutInvalidFullSpan) {
               return false;
            } else {
               byte var3;
               if (this.mShouldReverseLayout) {
                  var3 = -1;
               } else {
                  var3 = 1;
               }

               StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var4 = this.mLazySpanLookup.getFirstFullSpanItemInRange(var1, var2 + 1, var3, true);
               if (var4 == null) {
                  this.mLaidOutInvalidFullSpan = false;
                  this.mLazySpanLookup.forceInvalidateAfter(var2 + 1);
                  return false;
               } else {
                  StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var5 = this.mLazySpanLookup.getFirstFullSpanItemInRange(var1, var4.mPosition, var3 * -1, true);
                  if (var5 == null) {
                     this.mLazySpanLookup.forceInvalidateAfter(var4.mPosition);
                  } else {
                     this.mLazySpanLookup.forceInvalidateAfter(var5.mPosition + 1);
                  }

                  this.requestSimpleAnimationsInNextLayout();
                  this.requestLayout();
                  return true;
               }
            }
         }
      } else {
         return false;
      }
   }

   public boolean checkLayoutParams(RecyclerView.LayoutParams var1) {
      return var1 instanceof StaggeredGridLayoutManager.LayoutParams;
   }

   public void collectAdjacentPrefetchPositions(int var1, int var2, RecyclerView.State var3, RecyclerView.LayoutManager.LayoutPrefetchRegistry var4) {
      if (this.mOrientation != 0) {
         var1 = var2;
      }

      if (this.getChildCount() != 0) {
         if (var1 != 0) {
            this.prepareLayoutStateForDelta(var1, var3);
            int[] var6 = this.mPrefetchDistances;
            if (var6 == null || var6.length < this.mSpanCount) {
               this.mPrefetchDistances = new int[this.mSpanCount];
            }

            var1 = 0;

            for(var2 = 0; var2 < this.mSpanCount; ++var2) {
               int var5;
               if (this.mLayoutState.mItemDirection == -1) {
                  var5 = this.mLayoutState.mStartLine - this.mSpans[var2].getStartLine(this.mLayoutState.mStartLine);
               } else {
                  var5 = this.mSpans[var2].getEndLine(this.mLayoutState.mEndLine) - this.mLayoutState.mEndLine;
               }

               if (var5 >= 0) {
                  this.mPrefetchDistances[var1] = var5;
                  ++var1;
               }
            }

            Arrays.sort(this.mPrefetchDistances, 0, var1);

            for(var2 = 0; var2 < var1 && this.mLayoutState.hasMore(var3); ++var2) {
               var4.addPosition(this.mLayoutState.mCurrentPosition, this.mPrefetchDistances[var2]);
               LayoutState var7 = this.mLayoutState;
               var7.mCurrentPosition += this.mLayoutState.mItemDirection;
            }

         }
      }
   }

   public int computeHorizontalScrollExtent(RecyclerView.State var1) {
      return this.computeScrollExtent(var1);
   }

   public int computeHorizontalScrollOffset(RecyclerView.State var1) {
      return this.computeScrollOffset(var1);
   }

   public int computeHorizontalScrollRange(RecyclerView.State var1) {
      return this.computeScrollRange(var1);
   }

   public PointF computeScrollVectorForPosition(int var1) {
      var1 = this.calculateScrollDirectionForPosition(var1);
      PointF var2 = new PointF();
      if (var1 == 0) {
         return null;
      } else if (this.mOrientation == 0) {
         var2.x = (float)var1;
         var2.y = 0.0F;
         return var2;
      } else {
         var2.x = 0.0F;
         var2.y = (float)var1;
         return var2;
      }
   }

   public int computeVerticalScrollExtent(RecyclerView.State var1) {
      return this.computeScrollExtent(var1);
   }

   public int computeVerticalScrollOffset(RecyclerView.State var1) {
      return this.computeScrollOffset(var1);
   }

   public int computeVerticalScrollRange(RecyclerView.State var1) {
      return this.computeScrollRange(var1);
   }

   public int[] findFirstCompletelyVisibleItemPositions(int[] var1) {
      if (var1 == null) {
         var1 = new int[this.mSpanCount];
      } else if (var1.length < this.mSpanCount) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Provided int[]'s size must be more than or equal to span count. Expected:");
         var3.append(this.mSpanCount);
         var3.append(", array size:");
         var3.append(var1.length);
         throw new IllegalArgumentException(var3.toString());
      }

      for(int var2 = 0; var2 < this.mSpanCount; ++var2) {
         var1[var2] = this.mSpans[var2].findFirstCompletelyVisibleItemPosition();
      }

      return var1;
   }

   View findFirstVisibleItemClosestToEnd(boolean var1) {
      int var3 = this.mPrimaryOrientation.getStartAfterPadding();
      int var4 = this.mPrimaryOrientation.getEndAfterPadding();
      View var7 = null;

      for(int var2 = this.getChildCount() - 1; var2 >= 0; --var2) {
         View var8 = this.getChildAt(var2);
         int var5 = this.mPrimaryOrientation.getDecoratedStart(var8);
         int var6 = this.mPrimaryOrientation.getDecoratedEnd(var8);
         if (var6 > var3 && var5 < var4) {
            if (var6 <= var4) {
               return var8;
            }

            if (!var1) {
               return var8;
            }

            if (var7 == null) {
               var7 = var8;
            }
         }
      }

      return var7;
   }

   View findFirstVisibleItemClosestToStart(boolean var1) {
      int var3 = this.mPrimaryOrientation.getStartAfterPadding();
      int var4 = this.mPrimaryOrientation.getEndAfterPadding();
      int var5 = this.getChildCount();
      View var7 = null;

      for(int var2 = 0; var2 < var5; ++var2) {
         View var8 = this.getChildAt(var2);
         int var6 = this.mPrimaryOrientation.getDecoratedStart(var8);
         if (this.mPrimaryOrientation.getDecoratedEnd(var8) > var3 && var6 < var4) {
            if (var6 >= var3) {
               return var8;
            }

            if (!var1) {
               return var8;
            }

            if (var7 == null) {
               var7 = var8;
            }
         }
      }

      return var7;
   }

   int findFirstVisibleItemPositionInt() {
      View var1;
      if (this.mShouldReverseLayout) {
         var1 = this.findFirstVisibleItemClosestToEnd(true);
      } else {
         var1 = this.findFirstVisibleItemClosestToStart(true);
      }

      return var1 == null ? -1 : this.getPosition(var1);
   }

   public int[] findFirstVisibleItemPositions(int[] var1) {
      if (var1 == null) {
         var1 = new int[this.mSpanCount];
      } else if (var1.length < this.mSpanCount) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Provided int[]'s size must be more than or equal to span count. Expected:");
         var3.append(this.mSpanCount);
         var3.append(", array size:");
         var3.append(var1.length);
         throw new IllegalArgumentException(var3.toString());
      }

      for(int var2 = 0; var2 < this.mSpanCount; ++var2) {
         var1[var2] = this.mSpans[var2].findFirstVisibleItemPosition();
      }

      return var1;
   }

   public int[] findLastCompletelyVisibleItemPositions(int[] var1) {
      if (var1 == null) {
         var1 = new int[this.mSpanCount];
      } else if (var1.length < this.mSpanCount) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Provided int[]'s size must be more than or equal to span count. Expected:");
         var3.append(this.mSpanCount);
         var3.append(", array size:");
         var3.append(var1.length);
         throw new IllegalArgumentException(var3.toString());
      }

      for(int var2 = 0; var2 < this.mSpanCount; ++var2) {
         var1[var2] = this.mSpans[var2].findLastCompletelyVisibleItemPosition();
      }

      return var1;
   }

   public int[] findLastVisibleItemPositions(int[] var1) {
      if (var1 == null) {
         var1 = new int[this.mSpanCount];
      } else if (var1.length < this.mSpanCount) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Provided int[]'s size must be more than or equal to span count. Expected:");
         var3.append(this.mSpanCount);
         var3.append(", array size:");
         var3.append(var1.length);
         throw new IllegalArgumentException(var3.toString());
      }

      for(int var2 = 0; var2 < this.mSpanCount; ++var2) {
         var1[var2] = this.mSpans[var2].findLastVisibleItemPosition();
      }

      return var1;
   }

   public RecyclerView.LayoutParams generateDefaultLayoutParams() {
      return this.mOrientation == 0 ? new StaggeredGridLayoutManager.LayoutParams(-2, -1) : new StaggeredGridLayoutManager.LayoutParams(-1, -2);
   }

   public RecyclerView.LayoutParams generateLayoutParams(Context var1, AttributeSet var2) {
      return new StaggeredGridLayoutManager.LayoutParams(var1, var2);
   }

   public RecyclerView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof MarginLayoutParams ? new StaggeredGridLayoutManager.LayoutParams((MarginLayoutParams)var1) : new StaggeredGridLayoutManager.LayoutParams(var1);
   }

   public int getColumnCountForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.mOrientation == 1 ? this.mSpanCount : super.getColumnCountForAccessibility(var1, var2);
   }

   int getFirstChildPosition() {
      return this.getChildCount() == 0 ? 0 : this.getPosition(this.getChildAt(0));
   }

   public int getGapStrategy() {
      return this.mGapStrategy;
   }

   int getLastChildPosition() {
      int var1 = this.getChildCount();
      return var1 == 0 ? 0 : this.getPosition(this.getChildAt(var1 - 1));
   }

   public int getOrientation() {
      return this.mOrientation;
   }

   public boolean getReverseLayout() {
      return this.mReverseLayout;
   }

   public int getRowCountForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.mOrientation == 0 ? this.mSpanCount : super.getRowCountForAccessibility(var1, var2);
   }

   public int getSpanCount() {
      return this.mSpanCount;
   }

   View hasGapsToFix() {
      int var1 = this.getChildCount() - 1;
      BitSet var8 = new BitSet(this.mSpanCount);
      var8.set(0, this.mSpanCount, true);
      int var2 = this.mOrientation;
      byte var4 = -1;
      byte var13;
      if (var2 == 1 && this.isLayoutRTL()) {
         var13 = 1;
      } else {
         var13 = -1;
      }

      int var3;
      if (this.mShouldReverseLayout) {
         var3 = 0 - 1;
      } else {
         byte var5 = 0;
         var3 = var1 + 1;
         var1 = var5;
      }

      if (var1 < var3) {
         var4 = 1;
      }

      for(int var14 = var1; var14 != var3; var14 += var4) {
         View var9 = this.getChildAt(var14);
         StaggeredGridLayoutManager.LayoutParams var10 = (StaggeredGridLayoutManager.LayoutParams)var9.getLayoutParams();
         if (var8.get(var10.mSpan.mIndex)) {
            if (this.checkSpanForGap(var10.mSpan)) {
               return var9;
            }

            var8.clear(var10.mSpan.mIndex);
         }

         if (!var10.mFullSpan && var14 + var4 != var3) {
            View var11 = this.getChildAt(var14 + var4);
            boolean var6 = false;
            boolean var12 = false;
            int var7;
            if (this.mShouldReverseLayout) {
               int var15 = this.mPrimaryOrientation.getDecoratedEnd(var9);
               var7 = this.mPrimaryOrientation.getDecoratedEnd(var11);
               if (var15 < var7) {
                  return var9;
               }

               if (var15 == var7) {
                  var12 = true;
               }
            } else {
               var1 = this.mPrimaryOrientation.getDecoratedStart(var9);
               var7 = this.mPrimaryOrientation.getDecoratedStart(var11);
               if (var1 > var7) {
                  return var9;
               }

               if (var1 == var7) {
                  var12 = true;
               } else {
                  var12 = var6;
               }
            }

            if (var12) {
               StaggeredGridLayoutManager.LayoutParams var16 = (StaggeredGridLayoutManager.LayoutParams)var11.getLayoutParams();
               if (var10.mSpan.mIndex - var16.mSpan.mIndex < 0) {
                  var12 = true;
               } else {
                  var12 = false;
               }

               if (var13 < 0) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               if (var12 != var6) {
                  return var9;
               }
            }
         }
      }

      return null;
   }

   public void invalidateSpanAssignments() {
      this.mLazySpanLookup.clear();
      this.requestLayout();
   }

   boolean isLayoutRTL() {
      return this.getLayoutDirection() == 1;
   }

   public void offsetChildrenHorizontal(int var1) {
      super.offsetChildrenHorizontal(var1);

      for(int var2 = 0; var2 < this.mSpanCount; ++var2) {
         this.mSpans[var2].onOffset(var1);
      }

   }

   public void offsetChildrenVertical(int var1) {
      super.offsetChildrenVertical(var1);

      for(int var2 = 0; var2 < this.mSpanCount; ++var2) {
         this.mSpans[var2].onOffset(var1);
      }

   }

   public void onDetachedFromWindow(RecyclerView var1, RecyclerView.Recycler var2) {
      this.removeCallbacks(this.mCheckForGapsRunnable);

      for(int var3 = 0; var3 < this.mSpanCount; ++var3) {
         this.mSpans[var3].clear();
      }

      var1.requestLayout();
   }

   @Nullable
   public View onFocusSearchFailed(View var1, int var2, RecyclerView.Recycler var3, RecyclerView.State var4) {
      if (this.getChildCount() == 0) {
         return null;
      } else {
         var1 = this.findContainingItemView(var1);
         if (var1 == null) {
            return null;
         } else {
            this.resolveShouldLayoutReverse();
            int var7 = this.convertFocusDirectionToLayoutDirection(var2);
            if (var7 == Integer.MIN_VALUE) {
               return null;
            } else {
               StaggeredGridLayoutManager.LayoutParams var10 = (StaggeredGridLayoutManager.LayoutParams)var1.getLayoutParams();
               boolean var8 = var10.mFullSpan;
               StaggeredGridLayoutManager.Span var16 = var10.mSpan;
               if (var7 == 1) {
                  var2 = this.getLastChildPosition();
               } else {
                  var2 = this.getFirstChildPosition();
               }

               this.updateLayoutState(var2, var4);
               this.setLayoutStateDirection(var7);
               LayoutState var11 = this.mLayoutState;
               var11.mCurrentPosition = var11.mItemDirection + var2;
               this.mLayoutState.mAvailable = (int)((float)this.mPrimaryOrientation.getTotalSpace() * 0.33333334F);
               var11 = this.mLayoutState;
               var11.mStopInFocusable = true;
               boolean var6 = false;
               var11.mRecycle = false;
               this.fill(var3, var11, var4);
               this.mLastLayoutFromEnd = this.mShouldReverseLayout;
               View var12;
               if (!var8) {
                  var12 = var16.getFocusableViewAfter(var2, var7);
                  if (var12 != null && var12 != var1) {
                     return var12;
                  }
               }

               int var5;
               if (this.preferLastSpan(var7)) {
                  for(var5 = this.mSpanCount - 1; var5 >= 0; --var5) {
                     var12 = this.mSpans[var5].getFocusableViewAfter(var2, var7);
                     if (var12 != null && var12 != var1) {
                        return var12;
                     }
                  }
               } else {
                  for(var5 = 0; var5 < this.mSpanCount; ++var5) {
                     var12 = this.mSpans[var5].getFocusableViewAfter(var2, var7);
                     if (var12 != null && var12 != var1) {
                        return var12;
                     }
                  }
               }

               boolean var9 = this.mReverseLayout;
               boolean var14;
               if (var7 == -1) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               boolean var13 = var6;
               if ((var9 ^ true) == var14) {
                  var13 = true;
               }

               if (!var8) {
                  if (var13) {
                     var5 = var16.findFirstPartiallyVisibleItemPosition();
                  } else {
                     var5 = var16.findLastPartiallyVisibleItemPosition();
                  }

                  var12 = this.findViewByPosition(var5);
                  if (var12 != null && var12 != var1) {
                     return var12;
                  }
               }

               int var15;
               if (this.preferLastSpan(var7)) {
                  for(var5 = this.mSpanCount - 1; var5 >= 0; --var5) {
                     if (var5 != var16.mIndex) {
                        if (var13) {
                           var15 = this.mSpans[var5].findFirstPartiallyVisibleItemPosition();
                        } else {
                           var15 = this.mSpans[var5].findLastPartiallyVisibleItemPosition();
                        }

                        var12 = this.findViewByPosition(var15);
                        if (var12 != null && var12 != var1) {
                           return var12;
                        }
                     }
                  }

                  return null;
               } else {
                  for(var5 = 0; var5 < this.mSpanCount; ++var5) {
                     if (var13) {
                        var15 = this.mSpans[var5].findFirstPartiallyVisibleItemPosition();
                     } else {
                        var15 = this.mSpans[var5].findLastPartiallyVisibleItemPosition();
                     }

                     var12 = this.findViewByPosition(var15);
                     if (var12 != null && var12 != var1) {
                        return var12;
                     }
                  }

                  return null;
               }
            }
         }
      }
   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      super.onInitializeAccessibilityEvent(var1);
      if (this.getChildCount() > 0) {
         View var4 = this.findFirstVisibleItemClosestToStart(false);
         View var5 = this.findFirstVisibleItemClosestToEnd(false);
         if (var4 != null) {
            if (var5 != null) {
               int var2 = this.getPosition(var4);
               int var3 = this.getPosition(var5);
               if (var2 < var3) {
                  var1.setFromIndex(var2);
                  var1.setToIndex(var3);
               } else {
                  var1.setFromIndex(var3);
                  var1.setToIndex(var2);
               }
            }
         }
      }
   }

   public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler var1, RecyclerView.State var2, View var3, AccessibilityNodeInfoCompat var4) {
      android.view.ViewGroup.LayoutParams var7 = var3.getLayoutParams();
      if (!(var7 instanceof StaggeredGridLayoutManager.LayoutParams)) {
         super.onInitializeAccessibilityNodeInfoForItem(var3, var4);
      } else {
         StaggeredGridLayoutManager.LayoutParams var8 = (StaggeredGridLayoutManager.LayoutParams)var7;
         int var5;
         int var6;
         if (this.mOrientation == 0) {
            var6 = var8.getSpanIndex();
            if (var8.mFullSpan) {
               var5 = this.mSpanCount;
            } else {
               var5 = 1;
            }

            var4.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(var6, var5, -1, -1, var8.mFullSpan, false));
         } else {
            var6 = var8.getSpanIndex();
            if (var8.mFullSpan) {
               var5 = this.mSpanCount;
            } else {
               var5 = 1;
            }

            var4.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(-1, -1, var6, var5, var8.mFullSpan, false));
         }
      }
   }

   public void onItemsAdded(RecyclerView var1, int var2, int var3) {
      this.handleUpdate(var2, var3, 1);
   }

   public void onItemsChanged(RecyclerView var1) {
      this.mLazySpanLookup.clear();
      this.requestLayout();
   }

   public void onItemsMoved(RecyclerView var1, int var2, int var3, int var4) {
      this.handleUpdate(var2, var3, 8);
   }

   public void onItemsRemoved(RecyclerView var1, int var2, int var3) {
      this.handleUpdate(var2, var3, 2);
   }

   public void onItemsUpdated(RecyclerView var1, int var2, int var3, Object var4) {
      this.handleUpdate(var2, var3, 4);
   }

   public void onLayoutChildren(RecyclerView.Recycler var1, RecyclerView.State var2) {
      this.onLayoutChildren(var1, var2, true);
   }

   public void onLayoutCompleted(RecyclerView.State var1) {
      super.onLayoutCompleted(var1);
      this.mPendingScrollPosition = -1;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      this.mPendingSavedState = null;
      this.mAnchorInfo.reset();
   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (var1 instanceof StaggeredGridLayoutManager.SavedState) {
         this.mPendingSavedState = (StaggeredGridLayoutManager.SavedState)var1;
         this.requestLayout();
      }
   }

   public Parcelable onSaveInstanceState() {
      StaggeredGridLayoutManager.SavedState var3 = this.mPendingSavedState;
      if (var3 != null) {
         return new StaggeredGridLayoutManager.SavedState(var3);
      } else {
         var3 = new StaggeredGridLayoutManager.SavedState();
         var3.mReverseLayout = this.mReverseLayout;
         var3.mAnchorLayoutFromEnd = this.mLastLayoutFromEnd;
         var3.mLastLayoutRTL = this.mLastLayoutRTL;
         StaggeredGridLayoutManager.LazySpanLookup var4 = this.mLazySpanLookup;
         if (var4 != null && var4.mData != null) {
            var3.mSpanLookup = this.mLazySpanLookup.mData;
            var3.mSpanLookupSize = var3.mSpanLookup.length;
            var3.mFullSpanItems = this.mLazySpanLookup.mFullSpanItems;
         } else {
            var3.mSpanLookupSize = 0;
         }

         if (this.getChildCount() > 0) {
            int var1;
            if (this.mLastLayoutFromEnd) {
               var1 = this.getLastChildPosition();
            } else {
               var1 = this.getFirstChildPosition();
            }

            var3.mAnchorPosition = var1;
            var3.mVisibleAnchorPosition = this.findFirstVisibleItemPositionInt();
            var1 = this.mSpanCount;
            var3.mSpanOffsetsSize = var1;
            var3.mSpanOffsets = new int[var1];

            for(int var2 = 0; var2 < this.mSpanCount; ++var2) {
               if (this.mLastLayoutFromEnd) {
                  var1 = this.mSpans[var2].getEndLine(Integer.MIN_VALUE);
                  if (var1 != Integer.MIN_VALUE) {
                     var1 -= this.mPrimaryOrientation.getEndAfterPadding();
                  }
               } else {
                  var1 = this.mSpans[var2].getStartLine(Integer.MIN_VALUE);
                  if (var1 != Integer.MIN_VALUE) {
                     var1 -= this.mPrimaryOrientation.getStartAfterPadding();
                  }
               }

               var3.mSpanOffsets[var2] = var1;
            }

            return var3;
         } else {
            var3.mAnchorPosition = -1;
            var3.mVisibleAnchorPosition = -1;
            var3.mSpanOffsetsSize = 0;
            return var3;
         }
      }
   }

   public void onScrollStateChanged(int var1) {
      if (var1 == 0) {
         this.checkForGaps();
      }
   }

   void prepareLayoutStateForDelta(int var1, RecyclerView.State var2) {
      byte var3;
      int var4;
      if (var1 > 0) {
         var3 = 1;
         var4 = this.getLastChildPosition();
      } else {
         var3 = -1;
         var4 = this.getFirstChildPosition();
      }

      this.mLayoutState.mRecycle = true;
      this.updateLayoutState(var4, var2);
      this.setLayoutStateDirection(var3);
      LayoutState var5 = this.mLayoutState;
      var5.mCurrentPosition = var5.mItemDirection + var4;
      this.mLayoutState.mAvailable = Math.abs(var1);
   }

   int scrollBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      if (this.getChildCount() != 0) {
         if (var1 == 0) {
            return 0;
         } else {
            this.prepareLayoutStateForDelta(var1, var3);
            int var4 = this.fill(var2, this.mLayoutState, var3);
            if (this.mLayoutState.mAvailable >= var4) {
               if (var1 < 0) {
                  var1 = -var4;
               } else {
                  var1 = var4;
               }
            }

            this.mPrimaryOrientation.offsetChildren(-var1);
            this.mLastLayoutFromEnd = this.mShouldReverseLayout;
            LayoutState var5 = this.mLayoutState;
            var5.mAvailable = 0;
            this.recycle(var2, var5);
            return var1;
         }
      } else {
         return 0;
      }
   }

   public int scrollHorizontallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      return this.scrollBy(var1, var2, var3);
   }

   public void scrollToPosition(int var1) {
      StaggeredGridLayoutManager.SavedState var2 = this.mPendingSavedState;
      if (var2 != null && var2.mAnchorPosition != var1) {
         this.mPendingSavedState.invalidateAnchorPositionInfo();
      }

      this.mPendingScrollPosition = var1;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      this.requestLayout();
   }

   public void scrollToPositionWithOffset(int var1, int var2) {
      StaggeredGridLayoutManager.SavedState var3 = this.mPendingSavedState;
      if (var3 != null) {
         var3.invalidateAnchorPositionInfo();
      }

      this.mPendingScrollPosition = var1;
      this.mPendingScrollPositionOffset = var2;
      this.requestLayout();
   }

   public int scrollVerticallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      return this.scrollBy(var1, var2, var3);
   }

   public void setGapStrategy(int var1) {
      this.assertNotInLayoutOrScroll((String)null);
      if (var1 != this.mGapStrategy) {
         if (var1 != 0 && var1 != 2) {
            throw new IllegalArgumentException("invalid gap strategy. Must be GAP_HANDLING_NONE or GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS");
         } else {
            this.mGapStrategy = var1;
            boolean var2;
            if (this.mGapStrategy != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            this.setAutoMeasureEnabled(var2);
            this.requestLayout();
         }
      }
   }

   public void setMeasuredDimension(Rect var1, int var2, int var3) {
      int var4 = this.getPaddingLeft() + this.getPaddingRight();
      int var5 = this.getPaddingTop() + this.getPaddingBottom();
      if (this.mOrientation == 1) {
         var3 = chooseSize(var3, var1.height() + var5, this.getMinimumHeight());
         var2 = chooseSize(var2, this.mSizePerSpan * this.mSpanCount + var4, this.getMinimumWidth());
      } else {
         var2 = chooseSize(var2, var1.width() + var4, this.getMinimumWidth());
         var3 = chooseSize(var3, this.mSizePerSpan * this.mSpanCount + var5, this.getMinimumHeight());
      }

      this.setMeasuredDimension(var2, var3);
   }

   public void setOrientation(int var1) {
      if (var1 != 0 && var1 != 1) {
         throw new IllegalArgumentException("invalid orientation.");
      } else {
         this.assertNotInLayoutOrScroll((String)null);
         if (var1 != this.mOrientation) {
            this.mOrientation = var1;
            OrientationHelper var2 = this.mPrimaryOrientation;
            this.mPrimaryOrientation = this.mSecondaryOrientation;
            this.mSecondaryOrientation = var2;
            this.requestLayout();
         }
      }
   }

   public void setReverseLayout(boolean var1) {
      this.assertNotInLayoutOrScroll((String)null);
      StaggeredGridLayoutManager.SavedState var2 = this.mPendingSavedState;
      if (var2 != null && var2.mReverseLayout != var1) {
         this.mPendingSavedState.mReverseLayout = var1;
      }

      this.mReverseLayout = var1;
      this.requestLayout();
   }

   public void setSpanCount(int var1) {
      this.assertNotInLayoutOrScroll((String)null);
      if (var1 != this.mSpanCount) {
         this.invalidateSpanAssignments();
         this.mSpanCount = var1;
         this.mRemainingSpans = new BitSet(this.mSpanCount);
         this.mSpans = new StaggeredGridLayoutManager.Span[this.mSpanCount];

         for(var1 = 0; var1 < this.mSpanCount; ++var1) {
            this.mSpans[var1] = new StaggeredGridLayoutManager.Span(var1);
         }

         this.requestLayout();
      }
   }

   public void smoothScrollToPosition(RecyclerView var1, RecyclerView.State var2, int var3) {
      LinearSmoothScroller var4 = new LinearSmoothScroller(var1.getContext());
      var4.setTargetPosition(var3);
      this.startSmoothScroll(var4);
   }

   public boolean supportsPredictiveItemAnimations() {
      return this.mPendingSavedState == null;
   }

   boolean updateAnchorFromPendingData(RecyclerView.State var1, StaggeredGridLayoutManager.AnchorInfo var2) {
      boolean var5 = var1.isPreLayout();
      boolean var4 = false;
      if (!var5) {
         int var3 = this.mPendingScrollPosition;
         if (var3 == -1) {
            return false;
         } else if (var3 >= 0 && var3 < var1.getItemCount()) {
            StaggeredGridLayoutManager.SavedState var6 = this.mPendingSavedState;
            if (var6 != null && var6.mAnchorPosition != -1 && this.mPendingSavedState.mSpanOffsetsSize >= 1) {
               var2.mOffset = Integer.MIN_VALUE;
               var2.mPosition = this.mPendingScrollPosition;
               return true;
            } else {
               View var7 = this.findViewByPosition(this.mPendingScrollPosition);
               if (var7 != null) {
                  if (this.mShouldReverseLayout) {
                     var3 = this.getLastChildPosition();
                  } else {
                     var3 = this.getFirstChildPosition();
                  }

                  var2.mPosition = var3;
                  if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                     if (var2.mLayoutFromEnd) {
                        var2.mOffset = this.mPrimaryOrientation.getEndAfterPadding() - this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedEnd(var7);
                        return true;
                     }

                     var2.mOffset = this.mPrimaryOrientation.getStartAfterPadding() + this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedStart(var7);
                     return true;
                  }

                  if (this.mPrimaryOrientation.getDecoratedMeasurement(var7) > this.mPrimaryOrientation.getTotalSpace()) {
                     if (var2.mLayoutFromEnd) {
                        var3 = this.mPrimaryOrientation.getEndAfterPadding();
                     } else {
                        var3 = this.mPrimaryOrientation.getStartAfterPadding();
                     }

                     var2.mOffset = var3;
                     return true;
                  }

                  var3 = this.mPrimaryOrientation.getDecoratedStart(var7) - this.mPrimaryOrientation.getStartAfterPadding();
                  if (var3 < 0) {
                     var2.mOffset = -var3;
                     return true;
                  }

                  var3 = this.mPrimaryOrientation.getEndAfterPadding() - this.mPrimaryOrientation.getDecoratedEnd(var7);
                  if (var3 < 0) {
                     var2.mOffset = var3;
                     return true;
                  }

                  var2.mOffset = Integer.MIN_VALUE;
               } else {
                  var2.mPosition = this.mPendingScrollPosition;
                  var3 = this.mPendingScrollPositionOffset;
                  if (var3 == Integer.MIN_VALUE) {
                     if (this.calculateScrollDirectionForPosition(var2.mPosition) == 1) {
                        var4 = true;
                     }

                     var2.mLayoutFromEnd = var4;
                     var2.assignCoordinateFromPadding();
                  } else {
                     var2.assignCoordinateFromPadding(var3);
                  }

                  var2.mInvalidateOffsets = true;
               }

               return true;
            }
         } else {
            this.mPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            return false;
         }
      } else {
         return false;
      }
   }

   void updateAnchorInfoForLayout(RecyclerView.State var1, StaggeredGridLayoutManager.AnchorInfo var2) {
      if (!this.updateAnchorFromPendingData(var1, var2)) {
         if (!this.updateAnchorFromChildren(var1, var2)) {
            var2.assignCoordinateFromPadding();
            var2.mPosition = 0;
         }
      }
   }

   void updateMeasureSpecs(int var1) {
      this.mSizePerSpan = var1 / this.mSpanCount;
      this.mFullSizeSpec = MeasureSpec.makeMeasureSpec(var1, this.mSecondaryOrientation.getMode());
   }

   class AnchorInfo {
      boolean mInvalidateOffsets;
      boolean mLayoutFromEnd;
      int mOffset;
      int mPosition;
      int[] mSpanReferenceLines;
      boolean mValid;

      AnchorInfo() {
         this.reset();
      }

      void assignCoordinateFromPadding() {
         int var1;
         if (this.mLayoutFromEnd) {
            var1 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
         } else {
            var1 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
         }

         this.mOffset = var1;
      }

      void assignCoordinateFromPadding(int var1) {
         if (this.mLayoutFromEnd) {
            this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding() - var1;
         } else {
            this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding() + var1;
         }
      }

      void reset() {
         this.mPosition = -1;
         this.mOffset = Integer.MIN_VALUE;
         this.mLayoutFromEnd = false;
         this.mInvalidateOffsets = false;
         this.mValid = false;
         int[] var1 = this.mSpanReferenceLines;
         if (var1 != null) {
            Arrays.fill(var1, -1);
         }
      }

      void saveSpanReferenceLines(StaggeredGridLayoutManager.Span[] var1) {
         int var3 = var1.length;
         int[] var4 = this.mSpanReferenceLines;
         if (var4 == null || var4.length < var3) {
            this.mSpanReferenceLines = new int[StaggeredGridLayoutManager.this.mSpans.length];
         }

         for(int var2 = 0; var2 < var3; ++var2) {
            this.mSpanReferenceLines[var2] = var1[var2].getStartLine(Integer.MIN_VALUE);
         }

      }
   }

   public static class LayoutParams extends RecyclerView.LayoutParams {
      public static final int INVALID_SPAN_ID = -1;
      boolean mFullSpan;
      StaggeredGridLayoutManager.Span mSpan;

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

      public final int getSpanIndex() {
         StaggeredGridLayoutManager.Span var1 = this.mSpan;
         return var1 == null ? -1 : var1.mIndex;
      }

      public boolean isFullSpan() {
         return this.mFullSpan;
      }

      public void setFullSpan(boolean var1) {
         this.mFullSpan = var1;
      }
   }

   static class LazySpanLookup {
      private static final int MIN_SIZE = 10;
      int[] mData;
      List mFullSpanItems;

      private int invalidateFullSpansAfter(int var1) {
         if (this.mFullSpanItems == null) {
            return -1;
         } else {
            StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var6 = this.getFullSpanItem(var1);
            if (var6 != null) {
               this.mFullSpanItems.remove(var6);
            }

            byte var4 = -1;
            int var5 = this.mFullSpanItems.size();
            int var2 = 0;

            int var3;
            while(true) {
               var3 = var4;
               if (var2 >= var5) {
                  break;
               }

               if (((StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var2)).mPosition >= var1) {
                  var3 = var2;
                  break;
               }

               ++var2;
            }

            if (var3 != -1) {
               var6 = (StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var3);
               this.mFullSpanItems.remove(var3);
               return var6.mPosition;
            } else {
               return -1;
            }
         }
      }

      private void offsetFullSpansForAddition(int var1, int var2) {
         List var4 = this.mFullSpanItems;
         if (var4 != null) {
            for(int var3 = var4.size() - 1; var3 >= 0; --var3) {
               StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var5 = (StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var3);
               if (var5.mPosition >= var1) {
                  var5.mPosition += var2;
               }
            }

         }
      }

      private void offsetFullSpansForRemoval(int var1, int var2) {
         List var4 = this.mFullSpanItems;
         if (var4 != null) {
            for(int var3 = var4.size() - 1; var3 >= 0; --var3) {
               StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var5 = (StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var3);
               if (var5.mPosition >= var1) {
                  if (var5.mPosition < var1 + var2) {
                     this.mFullSpanItems.remove(var3);
                  } else {
                     var5.mPosition -= var2;
                  }
               }
            }

         }
      }

      public void addFullSpanItem(StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var1) {
         if (this.mFullSpanItems == null) {
            this.mFullSpanItems = new ArrayList();
         }

         int var3 = this.mFullSpanItems.size();

         for(int var2 = 0; var2 < var3; ++var2) {
            StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var4 = (StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var2);
            if (var4.mPosition == var1.mPosition) {
               this.mFullSpanItems.remove(var2);
            }

            if (var4.mPosition >= var1.mPosition) {
               this.mFullSpanItems.add(var2, var1);
               return;
            }
         }

         this.mFullSpanItems.add(var1);
      }

      void clear() {
         int[] var1 = this.mData;
         if (var1 != null) {
            Arrays.fill(var1, -1);
         }

         this.mFullSpanItems = null;
      }

      void ensureSize(int var1) {
         int[] var2 = this.mData;
         if (var2 == null) {
            this.mData = new int[Math.max(var1, 10) + 1];
            Arrays.fill(this.mData, -1);
         } else if (var1 >= var2.length) {
            var2 = this.mData;
            this.mData = new int[this.sizeForPosition(var1)];
            System.arraycopy(var2, 0, this.mData, 0, var2.length);
            int[] var3 = this.mData;
            Arrays.fill(var3, var2.length, var3.length, -1);
         }
      }

      int forceInvalidateAfter(int var1) {
         List var3 = this.mFullSpanItems;
         if (var3 != null) {
            for(int var2 = var3.size() - 1; var2 >= 0; --var2) {
               if (((StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var2)).mPosition >= var1) {
                  this.mFullSpanItems.remove(var2);
               }
            }
         }

         return this.invalidateAfter(var1);
      }

      public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem getFirstFullSpanItemInRange(int var1, int var2, int var3, boolean var4) {
         List var7 = this.mFullSpanItems;
         if (var7 == null) {
            return null;
         } else {
            int var6 = var7.size();

            for(int var5 = 0; var5 < var6; ++var5) {
               StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var8 = (StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var5);
               if (var8.mPosition >= var2) {
                  return null;
               }

               if (var8.mPosition >= var1 && (var3 == 0 || var8.mGapDir == var3 || var4 && var8.mHasUnwantedGapAfter)) {
                  return var8;
               }
            }

            return null;
         }
      }

      public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem getFullSpanItem(int var1) {
         List var3 = this.mFullSpanItems;
         if (var3 == null) {
            return null;
         } else {
            for(int var2 = var3.size() - 1; var2 >= 0; --var2) {
               StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var4 = (StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var2);
               if (var4.mPosition == var1) {
                  return var4;
               }
            }

            return null;
         }
      }

      int getSpan(int var1) {
         int[] var2 = this.mData;
         return var2 != null && var1 < var2.length ? var2[var1] : -1;
      }

      int invalidateAfter(int var1) {
         int[] var3 = this.mData;
         if (var3 == null) {
            return -1;
         } else if (var1 >= var3.length) {
            return -1;
         } else {
            int var2 = this.invalidateFullSpansAfter(var1);
            if (var2 == -1) {
               var3 = this.mData;
               Arrays.fill(var3, var1, var3.length, -1);
               return this.mData.length;
            } else {
               Arrays.fill(this.mData, var1, var2 + 1, -1);
               return var2 + 1;
            }
         }
      }

      void offsetForAddition(int var1, int var2) {
         int[] var3 = this.mData;
         if (var3 != null) {
            if (var1 < var3.length) {
               this.ensureSize(var1 + var2);
               var3 = this.mData;
               System.arraycopy(var3, var1, var3, var1 + var2, var3.length - var1 - var2);
               Arrays.fill(this.mData, var1, var1 + var2, -1);
               this.offsetFullSpansForAddition(var1, var2);
            }
         }
      }

      void offsetForRemoval(int var1, int var2) {
         int[] var3 = this.mData;
         if (var3 != null) {
            if (var1 < var3.length) {
               this.ensureSize(var1 + var2);
               var3 = this.mData;
               System.arraycopy(var3, var1 + var2, var3, var1, var3.length - var1 - var2);
               var3 = this.mData;
               Arrays.fill(var3, var3.length - var2, var3.length, -1);
               this.offsetFullSpansForRemoval(var1, var2);
            }
         }
      }

      void setSpan(int var1, StaggeredGridLayoutManager.Span var2) {
         this.ensureSize(var1);
         this.mData[var1] = var2.mIndex;
      }

      int sizeForPosition(int var1) {
         int var2;
         for(var2 = this.mData.length; var2 <= var1; var2 *= 2) {
         }

         return var2;
      }

      static class FullSpanItem implements Parcelable {
         public static final Creator CREATOR = new Creator() {
            public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFromParcel(Parcel var1) {
               return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem(var1);
            }

            public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[] newArray(int var1) {
               return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[var1];
            }
         };
         int mGapDir;
         int[] mGapPerSpan;
         boolean mHasUnwantedGapAfter;
         int mPosition;

         FullSpanItem() {
         }

         FullSpanItem(Parcel var1) {
            this.mPosition = var1.readInt();
            this.mGapDir = var1.readInt();
            int var2 = var1.readInt();
            boolean var3 = true;
            if (var2 != 1) {
               var3 = false;
            }

            this.mHasUnwantedGapAfter = var3;
            var2 = var1.readInt();
            if (var2 > 0) {
               this.mGapPerSpan = new int[var2];
               var1.readIntArray(this.mGapPerSpan);
            }
         }

         public int describeContents() {
            return 0;
         }

         int getGapForSpan(int var1) {
            int[] var2 = this.mGapPerSpan;
            return var2 == null ? 0 : var2[var1];
         }

         public String toString() {
            StringBuilder var1 = new StringBuilder();
            var1.append("FullSpanItem{mPosition=");
            var1.append(this.mPosition);
            var1.append(", mGapDir=");
            var1.append(this.mGapDir);
            var1.append(", mHasUnwantedGapAfter=");
            var1.append(this.mHasUnwantedGapAfter);
            var1.append(", mGapPerSpan=");
            var1.append(Arrays.toString(this.mGapPerSpan));
            var1.append('}');
            return var1.toString();
         }

         public void writeToParcel(Parcel var1, int var2) {
            throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
         }
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static class SavedState implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public StaggeredGridLayoutManager.SavedState createFromParcel(Parcel var1) {
            return new StaggeredGridLayoutManager.SavedState(var1);
         }

         public StaggeredGridLayoutManager.SavedState[] newArray(int var1) {
            return new StaggeredGridLayoutManager.SavedState[var1];
         }
      };
      boolean mAnchorLayoutFromEnd;
      int mAnchorPosition;
      List mFullSpanItems;
      boolean mLastLayoutRTL;
      boolean mReverseLayout;
      int[] mSpanLookup;
      int mSpanLookupSize;
      int[] mSpanOffsets;
      int mSpanOffsetsSize;
      int mVisibleAnchorPosition;

      public SavedState() {
      }

      SavedState(Parcel var1) {
         this.mAnchorPosition = var1.readInt();
         this.mVisibleAnchorPosition = var1.readInt();
         this.mSpanOffsetsSize = var1.readInt();
         int var2 = this.mSpanOffsetsSize;
         if (var2 > 0) {
            this.mSpanOffsets = new int[var2];
            var1.readIntArray(this.mSpanOffsets);
         }

         this.mSpanLookupSize = var1.readInt();
         var2 = this.mSpanLookupSize;
         if (var2 > 0) {
            this.mSpanLookup = new int[var2];
            var1.readIntArray(this.mSpanLookup);
         }

         var2 = var1.readInt();
         boolean var4 = false;
         boolean var3;
         if (var2 == 1) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.mReverseLayout = var3;
         if (var1.readInt() == 1) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.mAnchorLayoutFromEnd = var3;
         var3 = var4;
         if (var1.readInt() == 1) {
            var3 = true;
         }

         this.mLastLayoutRTL = var3;
         this.mFullSpanItems = var1.readArrayList(StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem.class.getClassLoader());
      }

      public SavedState(StaggeredGridLayoutManager.SavedState var1) {
         this.mSpanOffsetsSize = var1.mSpanOffsetsSize;
         this.mAnchorPosition = var1.mAnchorPosition;
         this.mVisibleAnchorPosition = var1.mVisibleAnchorPosition;
         this.mSpanOffsets = var1.mSpanOffsets;
         this.mSpanLookupSize = var1.mSpanLookupSize;
         this.mSpanLookup = var1.mSpanLookup;
         this.mReverseLayout = var1.mReverseLayout;
         this.mAnchorLayoutFromEnd = var1.mAnchorLayoutFromEnd;
         this.mLastLayoutRTL = var1.mLastLayoutRTL;
         this.mFullSpanItems = var1.mFullSpanItems;
      }

      public int describeContents() {
         return 0;
      }

      void invalidateAnchorPositionInfo() {
         this.mSpanOffsets = null;
         this.mSpanOffsetsSize = 0;
         this.mAnchorPosition = -1;
         this.mVisibleAnchorPosition = -1;
      }

      void invalidateSpanInfo() {
         this.mSpanOffsets = null;
         this.mSpanOffsetsSize = 0;
         this.mSpanLookupSize = 0;
         this.mSpanLookup = null;
         this.mFullSpanItems = null;
      }

      public void writeToParcel(Parcel var1, int var2) {
         throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
      }
   }

   class Span {
      static final int INVALID_LINE = Integer.MIN_VALUE;
      int mCachedEnd = Integer.MIN_VALUE;
      int mCachedStart = Integer.MIN_VALUE;
      int mDeletedSize = 0;
      final int mIndex;
      ArrayList mViews = new ArrayList();

      Span(int var2) {
         this.mIndex = var2;
      }

      void appendToSpan(View var1) {
         StaggeredGridLayoutManager.LayoutParams var2 = this.getLayoutParams(var1);
         var2.mSpan = this;
         this.mViews.add(var1);
         this.mCachedEnd = Integer.MIN_VALUE;
         if (this.mViews.size() == 1) {
            this.mCachedStart = Integer.MIN_VALUE;
         }

         if (var2.isItemRemoved() || var2.isItemChanged()) {
            this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(var1);
         }
      }

      void cacheReferenceLineAndClear(boolean var1, int var2) {
         int var3;
         if (var1) {
            var3 = this.getEndLine(Integer.MIN_VALUE);
         } else {
            var3 = this.getStartLine(Integer.MIN_VALUE);
         }

         this.clear();
         if (var3 != Integer.MIN_VALUE) {
            if (!var1 || var3 >= StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding()) {
               if (var1 || var3 <= StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding()) {
                  if (var2 != Integer.MIN_VALUE) {
                     var3 += var2;
                  }

                  this.mCachedEnd = var3;
                  this.mCachedStart = var3;
               }
            }
         }
      }

      void calculateCachedEnd() {
         ArrayList var1 = this.mViews;
         View var3 = (View)var1.get(var1.size() - 1);
         StaggeredGridLayoutManager.LayoutParams var2 = this.getLayoutParams(var3);
         this.mCachedEnd = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(var3);
         if (var2.mFullSpan) {
            StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var4 = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(var2.getViewLayoutPosition());
            if (var4 != null && var4.mGapDir == 1) {
               this.mCachedEnd += var4.getGapForSpan(this.mIndex);
            }
         }
      }

      void calculateCachedStart() {
         View var1 = (View)this.mViews.get(0);
         StaggeredGridLayoutManager.LayoutParams var2 = this.getLayoutParams(var1);
         this.mCachedStart = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(var1);
         if (var2.mFullSpan) {
            StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var3 = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(var2.getViewLayoutPosition());
            if (var3 != null && var3.mGapDir == -1) {
               this.mCachedStart -= var3.getGapForSpan(this.mIndex);
            }
         }
      }

      void clear() {
         this.mViews.clear();
         this.invalidateCache();
         this.mDeletedSize = 0;
      }

      public int findFirstCompletelyVisibleItemPosition() {
         return StaggeredGridLayoutManager.this.mReverseLayout ? this.findOneVisibleChild(this.mViews.size() - 1, -1, true) : this.findOneVisibleChild(0, this.mViews.size(), true);
      }

      public int findFirstPartiallyVisibleItemPosition() {
         return StaggeredGridLayoutManager.this.mReverseLayout ? this.findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true) : this.findOnePartiallyVisibleChild(0, this.mViews.size(), true);
      }

      public int findFirstVisibleItemPosition() {
         return StaggeredGridLayoutManager.this.mReverseLayout ? this.findOneVisibleChild(this.mViews.size() - 1, -1, false) : this.findOneVisibleChild(0, this.mViews.size(), false);
      }

      public int findLastCompletelyVisibleItemPosition() {
         return StaggeredGridLayoutManager.this.mReverseLayout ? this.findOneVisibleChild(0, this.mViews.size(), true) : this.findOneVisibleChild(this.mViews.size() - 1, -1, true);
      }

      public int findLastPartiallyVisibleItemPosition() {
         return StaggeredGridLayoutManager.this.mReverseLayout ? this.findOnePartiallyVisibleChild(0, this.mViews.size(), true) : this.findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true);
      }

      public int findLastVisibleItemPosition() {
         return StaggeredGridLayoutManager.this.mReverseLayout ? this.findOneVisibleChild(0, this.mViews.size(), false) : this.findOneVisibleChild(this.mViews.size() - 1, -1, false);
      }

      int findOnePartiallyOrCompletelyVisibleChild(int var1, int var2, boolean var3, boolean var4, boolean var5) {
         int var9 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
         int var10 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
         byte var6;
         if (var2 > var1) {
            var6 = 1;
         } else {
            var6 = -1;
         }

         for(; var1 != var2; var1 += var6) {
            boolean var7;
            boolean var8;
            int var11;
            int var12;
            View var13;
            label48: {
               label47: {
                  var13 = (View)this.mViews.get(var1);
                  var11 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(var13);
                  var12 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(var13);
                  var8 = false;
                  if (var5) {
                     if (var11 <= var10) {
                        break label47;
                     }
                  } else if (var11 < var10) {
                     break label47;
                  }

                  var7 = false;
                  break label48;
               }

               var7 = true;
            }

            label54: {
               if (var5) {
                  if (var12 < var9) {
                     break label54;
                  }
               } else if (var12 <= var9) {
                  break label54;
               }

               var8 = true;
            }

            if (var7 && var8) {
               if (var3 && var4) {
                  if (var11 >= var9 && var12 <= var10) {
                     return StaggeredGridLayoutManager.this.getPosition(var13);
                  }
               } else {
                  if (var4) {
                     return StaggeredGridLayoutManager.this.getPosition(var13);
                  }

                  if (var11 < var9 || var12 > var10) {
                     return StaggeredGridLayoutManager.this.getPosition(var13);
                  }
               }
            }
         }

         return -1;
      }

      int findOnePartiallyVisibleChild(int var1, int var2, boolean var3) {
         return this.findOnePartiallyOrCompletelyVisibleChild(var1, var2, false, false, var3);
      }

      int findOneVisibleChild(int var1, int var2, boolean var3) {
         return this.findOnePartiallyOrCompletelyVisibleChild(var1, var2, var3, true, false);
      }

      public int getDeletedSize() {
         return this.mDeletedSize;
      }

      int getEndLine() {
         int var1 = this.mCachedEnd;
         if (var1 != Integer.MIN_VALUE) {
            return var1;
         } else {
            this.calculateCachedEnd();
            return this.mCachedEnd;
         }
      }

      int getEndLine(int var1) {
         int var2 = this.mCachedEnd;
         if (var2 != Integer.MIN_VALUE) {
            return var2;
         } else if (this.mViews.size() == 0) {
            return var1;
         } else {
            this.calculateCachedEnd();
            return this.mCachedEnd;
         }
      }

      public View getFocusableViewAfter(int var1, int var2) {
         View var5 = null;
         View var4 = null;
         if (var2 != -1) {
            var2 = this.mViews.size() - 1;

            for(var4 = var5; var2 >= 0; --var2) {
               var5 = (View)this.mViews.get(var2);
               if (StaggeredGridLayoutManager.this.mReverseLayout && StaggeredGridLayoutManager.this.getPosition(var5) >= var1) {
                  return var4;
               }

               if (!StaggeredGridLayoutManager.this.mReverseLayout && StaggeredGridLayoutManager.this.getPosition(var5) <= var1) {
                  return var4;
               }

               if (!var5.hasFocusable()) {
                  return var4;
               }

               var4 = var5;
            }

            return var4;
         } else {
            int var3 = this.mViews.size();

            for(var2 = 0; var2 < var3; ++var2) {
               var5 = (View)this.mViews.get(var2);
               if (StaggeredGridLayoutManager.this.mReverseLayout && StaggeredGridLayoutManager.this.getPosition(var5) <= var1 || !StaggeredGridLayoutManager.this.mReverseLayout && StaggeredGridLayoutManager.this.getPosition(var5) >= var1 || !var5.hasFocusable()) {
                  break;
               }

               var4 = var5;
            }

            return var4;
         }
      }

      StaggeredGridLayoutManager.LayoutParams getLayoutParams(View var1) {
         return (StaggeredGridLayoutManager.LayoutParams)var1.getLayoutParams();
      }

      int getStartLine() {
         int var1 = this.mCachedStart;
         if (var1 != Integer.MIN_VALUE) {
            return var1;
         } else {
            this.calculateCachedStart();
            return this.mCachedStart;
         }
      }

      int getStartLine(int var1) {
         int var2 = this.mCachedStart;
         if (var2 != Integer.MIN_VALUE) {
            return var2;
         } else if (this.mViews.size() == 0) {
            return var1;
         } else {
            this.calculateCachedStart();
            return this.mCachedStart;
         }
      }

      void invalidateCache() {
         this.mCachedStart = Integer.MIN_VALUE;
         this.mCachedEnd = Integer.MIN_VALUE;
      }

      void onOffset(int var1) {
         int var2 = this.mCachedStart;
         if (var2 != Integer.MIN_VALUE) {
            this.mCachedStart = var2 + var1;
         }

         var2 = this.mCachedEnd;
         if (var2 != Integer.MIN_VALUE) {
            this.mCachedEnd = var2 + var1;
         }
      }

      void popEnd() {
         int var1 = this.mViews.size();
         View var2 = (View)this.mViews.remove(var1 - 1);
         StaggeredGridLayoutManager.LayoutParams var3 = this.getLayoutParams(var2);
         var3.mSpan = null;
         if (var3.isItemRemoved() || var3.isItemChanged()) {
            this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(var2);
         }

         if (var1 == 1) {
            this.mCachedStart = Integer.MIN_VALUE;
         }

         this.mCachedEnd = Integer.MIN_VALUE;
      }

      void popStart() {
         View var1 = (View)this.mViews.remove(0);
         StaggeredGridLayoutManager.LayoutParams var2 = this.getLayoutParams(var1);
         var2.mSpan = null;
         if (this.mViews.size() == 0) {
            this.mCachedEnd = Integer.MIN_VALUE;
         }

         if (var2.isItemRemoved() || var2.isItemChanged()) {
            this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(var1);
         }

         this.mCachedStart = Integer.MIN_VALUE;
      }

      void prependToSpan(View var1) {
         StaggeredGridLayoutManager.LayoutParams var2 = this.getLayoutParams(var1);
         var2.mSpan = this;
         this.mViews.add(0, var1);
         this.mCachedStart = Integer.MIN_VALUE;
         if (this.mViews.size() == 1) {
            this.mCachedEnd = Integer.MIN_VALUE;
         }

         if (var2.isItemRemoved() || var2.isItemChanged()) {
            this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(var1);
         }
      }

      void setLine(int var1) {
         this.mCachedStart = var1;
         this.mCachedEnd = var1;
      }
   }
}
