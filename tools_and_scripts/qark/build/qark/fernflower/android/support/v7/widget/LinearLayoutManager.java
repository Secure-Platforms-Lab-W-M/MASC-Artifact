package android.support.v7.widget;

import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import java.util.List;

public class LinearLayoutManager extends RecyclerView.LayoutManager implements ItemTouchHelper.ViewDropHandler, RecyclerView.SmoothScroller.ScrollVectorProvider {
   static final boolean DEBUG = false;
   public static final int HORIZONTAL = 0;
   public static final int INVALID_OFFSET = Integer.MIN_VALUE;
   private static final float MAX_SCROLL_FACTOR = 0.33333334F;
   private static final String TAG = "LinearLayoutManager";
   public static final int VERTICAL = 1;
   final LinearLayoutManager.AnchorInfo mAnchorInfo;
   private int mInitialPrefetchItemCount;
   private boolean mLastStackFromEnd;
   private final LinearLayoutManager.LayoutChunkResult mLayoutChunkResult;
   private LinearLayoutManager.LayoutState mLayoutState;
   int mOrientation;
   OrientationHelper mOrientationHelper;
   LinearLayoutManager.SavedState mPendingSavedState;
   int mPendingScrollPosition;
   int mPendingScrollPositionOffset;
   private boolean mRecycleChildrenOnDetach;
   private boolean mReverseLayout;
   boolean mShouldReverseLayout;
   private boolean mSmoothScrollbarEnabled;
   private boolean mStackFromEnd;

   public LinearLayoutManager(Context var1) {
      this(var1, 1, false);
   }

   public LinearLayoutManager(Context var1, int var2, boolean var3) {
      this.mReverseLayout = false;
      this.mShouldReverseLayout = false;
      this.mStackFromEnd = false;
      this.mSmoothScrollbarEnabled = true;
      this.mPendingScrollPosition = -1;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      this.mPendingSavedState = null;
      this.mAnchorInfo = new LinearLayoutManager.AnchorInfo();
      this.mLayoutChunkResult = new LinearLayoutManager.LayoutChunkResult();
      this.mInitialPrefetchItemCount = 2;
      this.setOrientation(var2);
      this.setReverseLayout(var3);
      this.setAutoMeasureEnabled(true);
   }

   public LinearLayoutManager(Context var1, AttributeSet var2, int var3, int var4) {
      this.mReverseLayout = false;
      this.mShouldReverseLayout = false;
      this.mStackFromEnd = false;
      this.mSmoothScrollbarEnabled = true;
      this.mPendingScrollPosition = -1;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      this.mPendingSavedState = null;
      this.mAnchorInfo = new LinearLayoutManager.AnchorInfo();
      this.mLayoutChunkResult = new LinearLayoutManager.LayoutChunkResult();
      this.mInitialPrefetchItemCount = 2;
      RecyclerView.LayoutManager.Properties var5 = getProperties(var1, var2, var3, var4);
      this.setOrientation(var5.orientation);
      this.setReverseLayout(var5.reverseLayout);
      this.setStackFromEnd(var5.stackFromEnd);
      this.setAutoMeasureEnabled(true);
   }

   private int computeScrollExtent(RecyclerView.State var1) {
      if (this.getChildCount() == 0) {
         return 0;
      } else {
         this.ensureLayoutState();
         return ScrollbarHelper.computeScrollExtent(var1, this.mOrientationHelper, this.findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), this.findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled);
      }
   }

   private int computeScrollOffset(RecyclerView.State var1) {
      if (this.getChildCount() == 0) {
         return 0;
      } else {
         this.ensureLayoutState();
         return ScrollbarHelper.computeScrollOffset(var1, this.mOrientationHelper, this.findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), this.findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
      }
   }

   private int computeScrollRange(RecyclerView.State var1) {
      if (this.getChildCount() == 0) {
         return 0;
      } else {
         this.ensureLayoutState();
         return ScrollbarHelper.computeScrollRange(var1, this.mOrientationHelper, this.findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), this.findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled);
      }
   }

   private View findFirstPartiallyOrCompletelyInvisibleChild(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.findOnePartiallyOrCompletelyInvisibleChild(0, this.getChildCount());
   }

   private View findFirstReferenceChild(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.findReferenceChild(var1, var2, 0, this.getChildCount(), var2.getItemCount());
   }

   private View findFirstVisibleChildClosestToEnd(boolean var1, boolean var2) {
      return this.mShouldReverseLayout ? this.findOneVisibleChild(0, this.getChildCount(), var1, var2) : this.findOneVisibleChild(this.getChildCount() - 1, -1, var1, var2);
   }

   private View findFirstVisibleChildClosestToStart(boolean var1, boolean var2) {
      return this.mShouldReverseLayout ? this.findOneVisibleChild(this.getChildCount() - 1, -1, var1, var2) : this.findOneVisibleChild(0, this.getChildCount(), var1, var2);
   }

   private View findLastPartiallyOrCompletelyInvisibleChild(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.findOnePartiallyOrCompletelyInvisibleChild(this.getChildCount() - 1, -1);
   }

   private View findLastReferenceChild(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.findReferenceChild(var1, var2, this.getChildCount() - 1, -1, var2.getItemCount());
   }

   private View findPartiallyOrCompletelyInvisibleChildClosestToEnd(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.mShouldReverseLayout ? this.findFirstPartiallyOrCompletelyInvisibleChild(var1, var2) : this.findLastPartiallyOrCompletelyInvisibleChild(var1, var2);
   }

   private View findPartiallyOrCompletelyInvisibleChildClosestToStart(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.mShouldReverseLayout ? this.findLastPartiallyOrCompletelyInvisibleChild(var1, var2) : this.findFirstPartiallyOrCompletelyInvisibleChild(var1, var2);
   }

   private View findReferenceChildClosestToEnd(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.mShouldReverseLayout ? this.findFirstReferenceChild(var1, var2) : this.findLastReferenceChild(var1, var2);
   }

   private View findReferenceChildClosestToStart(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.mShouldReverseLayout ? this.findLastReferenceChild(var1, var2) : this.findFirstReferenceChild(var1, var2);
   }

   private int fixLayoutEndGap(int var1, RecyclerView.Recycler var2, RecyclerView.State var3, boolean var4) {
      int var5 = this.mOrientationHelper.getEndAfterPadding() - var1;
      if (var5 > 0) {
         var5 = -this.scrollBy(-var5, var2, var3);
         if (var4) {
            var1 = this.mOrientationHelper.getEndAfterPadding() - (var1 + var5);
            if (var1 > 0) {
               this.mOrientationHelper.offsetChildren(var1);
               return var1 + var5;
            } else {
               return var5;
            }
         } else {
            return var5;
         }
      } else {
         return 0;
      }
   }

   private int fixLayoutStartGap(int var1, RecyclerView.Recycler var2, RecyclerView.State var3, boolean var4) {
      int var5 = var1 - this.mOrientationHelper.getStartAfterPadding();
      if (var5 > 0) {
         var5 = -this.scrollBy(var5, var2, var3);
         if (var4) {
            var1 = var1 + var5 - this.mOrientationHelper.getStartAfterPadding();
            if (var1 > 0) {
               this.mOrientationHelper.offsetChildren(-var1);
               return var5 - var1;
            } else {
               return var5;
            }
         } else {
            return var5;
         }
      } else {
         return 0;
      }
   }

   private View getChildClosestToEnd() {
      int var1;
      if (this.mShouldReverseLayout) {
         var1 = 0;
      } else {
         var1 = this.getChildCount() - 1;
      }

      return this.getChildAt(var1);
   }

   private View getChildClosestToStart() {
      int var1;
      if (this.mShouldReverseLayout) {
         var1 = this.getChildCount() - 1;
      } else {
         var1 = 0;
      }

      return this.getChildAt(var1);
   }

   private void layoutForPredictiveAnimations(RecyclerView.Recycler var1, RecyclerView.State var2, int var3, int var4) {
      if (var2.willRunPredictiveAnimations() && this.getChildCount() != 0 && !var2.isPreLayout()) {
         if (this.supportsPredictiveItemAnimations()) {
            int var7 = 0;
            int var6 = 0;
            List var13 = var1.getScrapList();
            int var9 = var13.size();
            int var10 = this.getPosition(this.getChildAt(0));

            for(int var5 = 0; var5 < var9; ++var5) {
               RecyclerView.ViewHolder var14 = (RecyclerView.ViewHolder)var13.get(var5);
               if (!var14.isRemoved()) {
                  int var11 = var14.getLayoutPosition();
                  byte var8 = 1;
                  boolean var12;
                  if (var11 < var10) {
                     var12 = true;
                  } else {
                     var12 = false;
                  }

                  if (var12 != this.mShouldReverseLayout) {
                     var8 = -1;
                  }

                  if (var8 == -1) {
                     var7 += this.mOrientationHelper.getDecoratedMeasurement(var14.itemView);
                  } else {
                     var6 += this.mOrientationHelper.getDecoratedMeasurement(var14.itemView);
                  }
               }
            }

            this.mLayoutState.mScrapList = var13;
            LinearLayoutManager.LayoutState var15;
            if (var7 > 0) {
               this.updateLayoutStateToFillStart(this.getPosition(this.getChildClosestToStart()), var3);
               var15 = this.mLayoutState;
               var15.mExtra = var7;
               var15.mAvailable = 0;
               var15.assignPositionFromScrapList();
               this.fill(var1, this.mLayoutState, var2, false);
            }

            if (var6 > 0) {
               this.updateLayoutStateToFillEnd(this.getPosition(this.getChildClosestToEnd()), var4);
               var15 = this.mLayoutState;
               var15.mExtra = var6;
               var15.mAvailable = 0;
               var15.assignPositionFromScrapList();
               this.fill(var1, this.mLayoutState, var2, false);
            }

            this.mLayoutState.mScrapList = null;
         }
      }
   }

   private void logChildren() {
      Log.d("LinearLayoutManager", "internal representation of views on the screen");

      for(int var1 = 0; var1 < this.getChildCount(); ++var1) {
         View var2 = this.getChildAt(var1);
         StringBuilder var3 = new StringBuilder();
         var3.append("item ");
         var3.append(this.getPosition(var2));
         var3.append(", coord:");
         var3.append(this.mOrientationHelper.getDecoratedStart(var2));
         Log.d("LinearLayoutManager", var3.toString());
      }

      Log.d("LinearLayoutManager", "==============");
   }

   private void recycleByLayoutState(RecyclerView.Recycler var1, LinearLayoutManager.LayoutState var2) {
      if (var2.mRecycle) {
         if (!var2.mInfinite) {
            if (var2.mLayoutDirection == -1) {
               this.recycleViewsFromEnd(var1, var2.mScrollingOffset);
            } else {
               this.recycleViewsFromStart(var1, var2.mScrollingOffset);
            }
         }
      }
   }

   private void recycleChildren(RecyclerView.Recycler var1, int var2, int var3) {
      if (var2 != var3) {
         if (var3 > var2) {
            --var3;

            while(var3 >= var2) {
               this.removeAndRecycleViewAt(var3, var1);
               --var3;
            }

         } else {
            while(var2 > var3) {
               this.removeAndRecycleViewAt(var2, var1);
               --var2;
            }

         }
      }
   }

   private void recycleViewsFromEnd(RecyclerView.Recycler var1, int var2) {
      int var3 = this.getChildCount();
      if (var2 >= 0) {
         int var4 = this.mOrientationHelper.getEnd() - var2;
         View var5;
         if (this.mShouldReverseLayout) {
            for(var2 = 0; var2 < var3; ++var2) {
               var5 = this.getChildAt(var2);
               if (this.mOrientationHelper.getDecoratedStart(var5) < var4 || this.mOrientationHelper.getTransformedStartWithDecoration(var5) < var4) {
                  this.recycleChildren(var1, 0, var2);
                  return;
               }
            }

         } else {
            for(var2 = var3 - 1; var2 >= 0; --var2) {
               var5 = this.getChildAt(var2);
               if (this.mOrientationHelper.getDecoratedStart(var5) < var4 || this.mOrientationHelper.getTransformedStartWithDecoration(var5) < var4) {
                  this.recycleChildren(var1, var3 - 1, var2);
                  return;
               }
            }

         }
      }
   }

   private void recycleViewsFromStart(RecyclerView.Recycler var1, int var2) {
      if (var2 >= 0) {
         int var4 = this.getChildCount();
         int var3;
         View var5;
         if (this.mShouldReverseLayout) {
            for(var3 = var4 - 1; var3 >= 0; --var3) {
               var5 = this.getChildAt(var3);
               if (this.mOrientationHelper.getDecoratedEnd(var5) > var2 || this.mOrientationHelper.getTransformedEndWithDecoration(var5) > var2) {
                  this.recycleChildren(var1, var4 - 1, var3);
                  return;
               }
            }

         } else {
            for(var3 = 0; var3 < var4; ++var3) {
               var5 = this.getChildAt(var3);
               if (this.mOrientationHelper.getDecoratedEnd(var5) > var2 || this.mOrientationHelper.getTransformedEndWithDecoration(var5) > var2) {
                  this.recycleChildren(var1, 0, var3);
                  return;
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

   private boolean updateAnchorFromChildren(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.AnchorInfo var3) {
      int var5 = this.getChildCount();
      boolean var4 = false;
      if (var5 == 0) {
         return false;
      } else {
         View var6 = this.getFocusedChild();
         if (var6 != null && var3.isViewValidAsAnchor(var6, var2)) {
            var3.assignFromViewAndKeepVisibleRect(var6);
            return true;
         } else if (this.mLastStackFromEnd != this.mStackFromEnd) {
            return false;
         } else {
            View var7;
            if (var3.mLayoutFromEnd) {
               var7 = this.findReferenceChildClosestToEnd(var1, var2);
            } else {
               var7 = this.findReferenceChildClosestToStart(var1, var2);
            }

            if (var7 == null) {
               return false;
            } else {
               var3.assignFromView(var7);
               if (!var2.isPreLayout() && this.supportsPredictiveItemAnimations()) {
                  if (this.mOrientationHelper.getDecoratedStart(var7) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(var7) < this.mOrientationHelper.getStartAfterPadding()) {
                     var4 = true;
                  }

                  if (var4) {
                     int var8;
                     if (var3.mLayoutFromEnd) {
                        var8 = this.mOrientationHelper.getEndAfterPadding();
                     } else {
                        var8 = this.mOrientationHelper.getStartAfterPadding();
                     }

                     var3.mCoordinate = var8;
                     return true;
                  } else {
                     return true;
                  }
               } else {
                  return true;
               }
            }
         }
      }
   }

   private boolean updateAnchorFromPendingData(RecyclerView.State var1, LinearLayoutManager.AnchorInfo var2) {
      boolean var4 = var1.isPreLayout();
      boolean var5 = false;
      if (!var4) {
         int var3 = this.mPendingScrollPosition;
         if (var3 == -1) {
            return false;
         } else if (var3 >= 0 && var3 < var1.getItemCount()) {
            var2.mPosition = this.mPendingScrollPosition;
            LinearLayoutManager.SavedState var6 = this.mPendingSavedState;
            if (var6 != null && var6.hasValidAnchor()) {
               var2.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
               if (var2.mLayoutFromEnd) {
                  var2.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingSavedState.mAnchorOffset;
                  return true;
               } else {
                  var2.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingSavedState.mAnchorOffset;
                  return true;
               }
            } else if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
               View var7 = this.findViewByPosition(this.mPendingScrollPosition);
               if (var7 != null) {
                  if (this.mOrientationHelper.getDecoratedMeasurement(var7) > this.mOrientationHelper.getTotalSpace()) {
                     var2.assignCoordinateFromPadding();
                     return true;
                  } else if (this.mOrientationHelper.getDecoratedStart(var7) - this.mOrientationHelper.getStartAfterPadding() < 0) {
                     var2.mCoordinate = this.mOrientationHelper.getStartAfterPadding();
                     var2.mLayoutFromEnd = false;
                     return true;
                  } else if (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(var7) < 0) {
                     var2.mCoordinate = this.mOrientationHelper.getEndAfterPadding();
                     var2.mLayoutFromEnd = true;
                     return true;
                  } else {
                     if (var2.mLayoutFromEnd) {
                        var3 = this.mOrientationHelper.getDecoratedEnd(var7) + this.mOrientationHelper.getTotalSpaceChange();
                     } else {
                        var3 = this.mOrientationHelper.getDecoratedStart(var7);
                     }

                     var2.mCoordinate = var3;
                     return true;
                  }
               } else {
                  if (this.getChildCount() > 0) {
                     var3 = this.getPosition(this.getChildAt(0));
                     if (this.mPendingScrollPosition < var3) {
                        var4 = true;
                     } else {
                        var4 = false;
                     }

                     if (var4 == this.mShouldReverseLayout) {
                        var5 = true;
                     }

                     var2.mLayoutFromEnd = var5;
                  }

                  var2.assignCoordinateFromPadding();
                  return true;
               }
            } else {
               var4 = this.mShouldReverseLayout;
               var2.mLayoutFromEnd = var4;
               if (var4) {
                  var2.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingScrollPositionOffset;
                  return true;
               } else {
                  var2.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingScrollPositionOffset;
                  return true;
               }
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

   private void updateAnchorInfoForLayout(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.AnchorInfo var3) {
      if (!this.updateAnchorFromPendingData(var2, var3)) {
         if (!this.updateAnchorFromChildren(var1, var2, var3)) {
            var3.assignCoordinateFromPadding();
            int var4;
            if (this.mStackFromEnd) {
               var4 = var2.getItemCount() - 1;
            } else {
               var4 = 0;
            }

            var3.mPosition = var4;
         }
      }
   }

   private void updateLayoutState(int var1, int var2, boolean var3, RecyclerView.State var4) {
      this.mLayoutState.mInfinite = this.resolveIsInfinite();
      this.mLayoutState.mExtra = this.getExtraLayoutSpace(var4);
      LinearLayoutManager.LayoutState var7 = this.mLayoutState;
      var7.mLayoutDirection = var1;
      byte var5 = -1;
      LinearLayoutManager.LayoutState var6;
      View var8;
      if (var1 == 1) {
         var7.mExtra += this.mOrientationHelper.getEndPadding();
         var8 = this.getChildClosestToEnd();
         var6 = this.mLayoutState;
         if (!this.mShouldReverseLayout) {
            var5 = 1;
         }

         var6.mItemDirection = var5;
         this.mLayoutState.mCurrentPosition = this.getPosition(var8) + this.mLayoutState.mItemDirection;
         this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedEnd(var8);
         var1 = this.mOrientationHelper.getDecoratedEnd(var8) - this.mOrientationHelper.getEndAfterPadding();
      } else {
         var8 = this.getChildClosestToStart();
         var6 = this.mLayoutState;
         var6.mExtra += this.mOrientationHelper.getStartAfterPadding();
         var6 = this.mLayoutState;
         if (this.mShouldReverseLayout) {
            var5 = 1;
         }

         var6.mItemDirection = var5;
         this.mLayoutState.mCurrentPosition = this.getPosition(var8) + this.mLayoutState.mItemDirection;
         this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedStart(var8);
         var1 = -this.mOrientationHelper.getDecoratedStart(var8) + this.mOrientationHelper.getStartAfterPadding();
      }

      var7 = this.mLayoutState;
      var7.mAvailable = var2;
      if (var3) {
         var7.mAvailable -= var1;
      }

      this.mLayoutState.mScrollingOffset = var1;
   }

   private void updateLayoutStateToFillEnd(int var1, int var2) {
      this.mLayoutState.mAvailable = this.mOrientationHelper.getEndAfterPadding() - var2;
      LinearLayoutManager.LayoutState var4 = this.mLayoutState;
      byte var3;
      if (this.mShouldReverseLayout) {
         var3 = -1;
      } else {
         var3 = 1;
      }

      var4.mItemDirection = var3;
      var4 = this.mLayoutState;
      var4.mCurrentPosition = var1;
      var4.mLayoutDirection = 1;
      var4.mOffset = var2;
      var4.mScrollingOffset = Integer.MIN_VALUE;
   }

   private void updateLayoutStateToFillEnd(LinearLayoutManager.AnchorInfo var1) {
      this.updateLayoutStateToFillEnd(var1.mPosition, var1.mCoordinate);
   }

   private void updateLayoutStateToFillStart(int var1, int var2) {
      this.mLayoutState.mAvailable = var2 - this.mOrientationHelper.getStartAfterPadding();
      LinearLayoutManager.LayoutState var3 = this.mLayoutState;
      var3.mCurrentPosition = var1;
      byte var4;
      if (this.mShouldReverseLayout) {
         var4 = 1;
      } else {
         var4 = -1;
      }

      var3.mItemDirection = var4;
      var3 = this.mLayoutState;
      var3.mLayoutDirection = -1;
      var3.mOffset = var2;
      var3.mScrollingOffset = Integer.MIN_VALUE;
   }

   private void updateLayoutStateToFillStart(LinearLayoutManager.AnchorInfo var1) {
      this.updateLayoutStateToFillStart(var1.mPosition, var1.mCoordinate);
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

   public void collectAdjacentPrefetchPositions(int var1, int var2, RecyclerView.State var3, RecyclerView.LayoutManager.LayoutPrefetchRegistry var4) {
      if (this.mOrientation != 0) {
         var1 = var2;
      }

      if (this.getChildCount() != 0) {
         if (var1 != 0) {
            this.ensureLayoutState();
            byte var5;
            if (var1 > 0) {
               var5 = 1;
            } else {
               var5 = -1;
            }

            this.updateLayoutState(var5, Math.abs(var1), true, var3);
            this.collectPrefetchPositionsForLayoutState(var3, this.mLayoutState, var4);
         }
      }
   }

   public void collectInitialPrefetchPositions(int var1, RecyclerView.LayoutManager.LayoutPrefetchRegistry var2) {
      LinearLayoutManager.SavedState var7 = this.mPendingSavedState;
      byte var4 = -1;
      int var3;
      boolean var6;
      if (var7 != null && var7.hasValidAnchor()) {
         var6 = this.mPendingSavedState.mAnchorLayoutFromEnd;
         var3 = this.mPendingSavedState.mAnchorPosition;
      } else {
         this.resolveShouldLayoutReverse();
         var6 = this.mShouldReverseLayout;
         if (this.mPendingScrollPosition == -1) {
            if (var6) {
               var3 = var1 - 1;
            } else {
               var3 = 0;
            }
         } else {
            var3 = this.mPendingScrollPosition;
         }
      }

      if (!var6) {
         var4 = 1;
      }

      int var5 = var3;

      for(var3 = 0; var3 < this.mInitialPrefetchItemCount; ++var3) {
         if (var5 < 0 || var5 >= var1) {
            return;
         }

         var2.addPosition(var5, 0);
         var5 += var4;
      }

   }

   void collectPrefetchPositionsForLayoutState(RecyclerView.State var1, LinearLayoutManager.LayoutState var2, RecyclerView.LayoutManager.LayoutPrefetchRegistry var3) {
      int var4 = var2.mCurrentPosition;
      if (var4 >= 0 && var4 < var1.getItemCount()) {
         var3.addPosition(var4, Math.max(0, var2.mScrollingOffset));
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
      if (this.getChildCount() == 0) {
         return null;
      } else {
         boolean var4 = false;
         int var3 = this.getPosition(this.getChildAt(0));
         byte var2 = 1;
         if (var1 < var3) {
            var4 = true;
         }

         byte var5 = var2;
         if (var4 != this.mShouldReverseLayout) {
            var5 = -1;
         }

         return this.mOrientation == 0 ? new PointF((float)var5, 0.0F) : new PointF(0.0F, (float)var5);
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

   int convertFocusDirectionToLayoutDirection(int var1) {
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

   LinearLayoutManager.LayoutState createLayoutState() {
      return new LinearLayoutManager.LayoutState();
   }

   void ensureLayoutState() {
      if (this.mLayoutState == null) {
         this.mLayoutState = this.createLayoutState();
      }

      if (this.mOrientationHelper == null) {
         this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, this.mOrientation);
      }
   }

   int fill(RecyclerView.Recycler var1, LinearLayoutManager.LayoutState var2, RecyclerView.State var3, boolean var4) {
      int var6 = var2.mAvailable;
      if (var2.mScrollingOffset != Integer.MIN_VALUE) {
         if (var2.mAvailable < 0) {
            var2.mScrollingOffset += var2.mAvailable;
         }

         this.recycleByLayoutState(var1, var2);
      }

      int var5 = var2.mAvailable + var2.mExtra;
      LinearLayoutManager.LayoutChunkResult var7 = this.mLayoutChunkResult;

      while((var2.mInfinite || var5 > 0) && var2.hasMore(var3)) {
         var7.resetInternal();
         this.layoutChunk(var1, var3, var2, var7);
         if (var7.mFinished) {
            break;
         }

         var2.mOffset += var7.mConsumed * var2.mLayoutDirection;
         if (!var7.mIgnoreConsumed || this.mLayoutState.mScrapList != null || !var3.isPreLayout()) {
            var2.mAvailable -= var7.mConsumed;
            var5 -= var7.mConsumed;
         }

         if (var2.mScrollingOffset != Integer.MIN_VALUE) {
            var2.mScrollingOffset += var7.mConsumed;
            if (var2.mAvailable < 0) {
               var2.mScrollingOffset += var2.mAvailable;
            }

            this.recycleByLayoutState(var1, var2);
         }

         if (var4 && var7.mFocusable) {
            break;
         }
      }

      return var6 - var2.mAvailable;
   }

   public int findFirstCompletelyVisibleItemPosition() {
      View var1 = this.findOneVisibleChild(0, this.getChildCount(), true, false);
      return var1 == null ? -1 : this.getPosition(var1);
   }

   public int findFirstVisibleItemPosition() {
      View var1 = this.findOneVisibleChild(0, this.getChildCount(), false, true);
      return var1 == null ? -1 : this.getPosition(var1);
   }

   public int findLastCompletelyVisibleItemPosition() {
      View var1 = this.findOneVisibleChild(this.getChildCount() - 1, -1, true, false);
      return var1 == null ? -1 : this.getPosition(var1);
   }

   public int findLastVisibleItemPosition() {
      View var1 = this.findOneVisibleChild(this.getChildCount() - 1, -1, false, true);
      return var1 == null ? -1 : this.getPosition(var1);
   }

   View findOnePartiallyOrCompletelyInvisibleChild(int var1, int var2) {
      this.ensureLayoutState();
      byte var3;
      if (var2 > var1) {
         var3 = 1;
      } else if (var2 < var1) {
         var3 = -1;
      } else {
         var3 = 0;
      }

      if (var3 == 0) {
         return this.getChildAt(var1);
      } else {
         short var4;
         short var5;
         if (this.mOrientationHelper.getDecoratedStart(this.getChildAt(var1)) < this.mOrientationHelper.getStartAfterPadding()) {
            var5 = 16644;
            var4 = 16388;
         } else {
            var5 = 4161;
            var4 = 4097;
         }

         return this.mOrientation == 0 ? this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(var1, var2, var5, var4) : this.mVerticalBoundCheck.findOneViewWithinBoundFlags(var1, var2, var5, var4);
      }
   }

   View findOneVisibleChild(int var1, int var2, boolean var3, boolean var4) {
      this.ensureLayoutState();
      short var6 = 0;
      short var5;
      if (var3) {
         var5 = 24579;
      } else {
         var5 = 320;
      }

      if (var4) {
         var6 = 320;
      }

      return this.mOrientation == 0 ? this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(var1, var2, var5, var6) : this.mVerticalBoundCheck.findOneViewWithinBoundFlags(var1, var2, var5, var6);
   }

   View findReferenceChild(RecyclerView.Recycler var1, RecyclerView.State var2, int var3, int var4, int var5) {
      this.ensureLayoutState();
      View var12 = null;
      View var11 = null;
      int var7 = this.mOrientationHelper.getStartAfterPadding();
      int var8 = this.mOrientationHelper.getEndAfterPadding();
      byte var6;
      if (var4 > var3) {
         var6 = 1;
      } else {
         var6 = -1;
      }

      for(; var3 != var4; var3 += var6) {
         View var10 = this.getChildAt(var3);
         int var9 = this.getPosition(var10);
         if (var9 >= 0 && var9 < var5) {
            if (((RecyclerView.LayoutParams)var10.getLayoutParams()).isItemRemoved()) {
               if (var12 == null) {
                  var12 = var10;
               }
            } else {
               if (this.mOrientationHelper.getDecoratedStart(var10) < var8 && this.mOrientationHelper.getDecoratedEnd(var10) >= var7) {
                  return var10;
               }

               if (var11 == null) {
                  var11 = var10;
               }
            }
         }
      }

      if (var11 != null) {
         return var11;
      } else {
         return var12;
      }
   }

   public View findViewByPosition(int var1) {
      int var2 = this.getChildCount();
      if (var2 == 0) {
         return null;
      } else {
         int var3 = var1 - this.getPosition(this.getChildAt(0));
         if (var3 >= 0 && var3 < var2) {
            View var4 = this.getChildAt(var3);
            if (this.getPosition(var4) == var1) {
               return var4;
            }
         }

         return super.findViewByPosition(var1);
      }
   }

   public RecyclerView.LayoutParams generateDefaultLayoutParams() {
      return new RecyclerView.LayoutParams(-2, -2);
   }

   protected int getExtraLayoutSpace(RecyclerView.State var1) {
      return var1.hasTargetScrollPosition() ? this.mOrientationHelper.getTotalSpace() : 0;
   }

   public int getInitialPrefetchItemCount() {
      return this.mInitialPrefetchItemCount;
   }

   public int getOrientation() {
      return this.mOrientation;
   }

   public boolean getRecycleChildrenOnDetach() {
      return this.mRecycleChildrenOnDetach;
   }

   public boolean getReverseLayout() {
      return this.mReverseLayout;
   }

   public boolean getStackFromEnd() {
      return this.mStackFromEnd;
   }

   protected boolean isLayoutRTL() {
      return this.getLayoutDirection() == 1;
   }

   public boolean isSmoothScrollbarEnabled() {
      return this.mSmoothScrollbarEnabled;
   }

   void layoutChunk(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.LayoutState var3, LinearLayoutManager.LayoutChunkResult var4) {
      View var13 = var3.next(var1);
      if (var13 == null) {
         var4.mFinished = true;
      } else {
         RecyclerView.LayoutParams var14 = (RecyclerView.LayoutParams)var13.getLayoutParams();
         boolean var11;
         boolean var12;
         if (var3.mScrapList == null) {
            var12 = this.mShouldReverseLayout;
            if (var3.mLayoutDirection == -1) {
               var11 = true;
            } else {
               var11 = false;
            }

            if (var12 == var11) {
               this.addView(var13);
            } else {
               this.addView(var13, 0);
            }
         } else {
            var12 = this.mShouldReverseLayout;
            if (var3.mLayoutDirection == -1) {
               var11 = true;
            } else {
               var11 = false;
            }

            if (var12 == var11) {
               this.addDisappearingView(var13);
            } else {
               this.addDisappearingView(var13, 0);
            }
         }

         this.measureChildWithMargins(var13, 0, 0);
         var4.mConsumed = this.mOrientationHelper.getDecoratedMeasurement(var13);
         int var5;
         int var6;
         int var7;
         int var8;
         int var9;
         int var10;
         if (this.mOrientation == 1) {
            if (this.isLayoutRTL()) {
               var6 = this.getWidth() - this.getPaddingRight();
               var5 = var6 - this.mOrientationHelper.getDecoratedMeasurementInOther(var13);
            } else {
               var5 = this.getPaddingLeft();
               var6 = this.mOrientationHelper.getDecoratedMeasurementInOther(var13) + var5;
            }

            if (var3.mLayoutDirection == -1) {
               var8 = var3.mOffset;
               var9 = var3.mOffset;
               var10 = var4.mConsumed;
               var7 = var6;
               var6 = var9 - var10;
            } else {
               var8 = var3.mOffset;
               var9 = var3.mOffset;
               var10 = var4.mConsumed;
               var7 = var6;
               var6 = var8;
               var8 = var9 + var10;
            }
         } else {
            var6 = this.getPaddingTop();
            var5 = this.mOrientationHelper.getDecoratedMeasurementInOther(var13) + var6;
            if (var3.mLayoutDirection == -1) {
               var7 = var3.mOffset;
               var9 = var3.mOffset;
               var10 = var4.mConsumed;
               var8 = var5;
               var5 = var9 - var10;
            } else {
               var9 = var3.mOffset;
               var7 = var3.mOffset;
               var8 = var4.mConsumed;
               var7 += var8;
               var8 = var5;
               var5 = var9;
            }
         }

         this.layoutDecoratedWithMargins(var13, var5, var6, var7, var8);
         if (var14.isItemRemoved() || var14.isItemChanged()) {
            var4.mIgnoreConsumed = true;
         }

         var4.mFocusable = var13.hasFocusable();
      }
   }

   void onAnchorReady(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.AnchorInfo var3, int var4) {
   }

   public void onDetachedFromWindow(RecyclerView var1, RecyclerView.Recycler var2) {
      super.onDetachedFromWindow(var1, var2);
      if (this.mRecycleChildrenOnDetach) {
         this.removeAndRecycleAllViews(var2);
         var2.clear();
      }
   }

   public View onFocusSearchFailed(View var1, int var2, RecyclerView.Recycler var3, RecyclerView.State var4) {
      this.resolveShouldLayoutReverse();
      if (this.getChildCount() == 0) {
         return null;
      } else {
         var2 = this.convertFocusDirectionToLayoutDirection(var2);
         if (var2 == Integer.MIN_VALUE) {
            return null;
         } else {
            this.ensureLayoutState();
            this.ensureLayoutState();
            this.updateLayoutState(var2, (int)((float)this.mOrientationHelper.getTotalSpace() * 0.33333334F), false, var4);
            LinearLayoutManager.LayoutState var5 = this.mLayoutState;
            var5.mScrollingOffset = Integer.MIN_VALUE;
            var5.mRecycle = false;
            this.fill(var3, var5, var4, true);
            if (var2 == -1) {
               var1 = this.findPartiallyOrCompletelyInvisibleChildClosestToStart(var3, var4);
            } else {
               var1 = this.findPartiallyOrCompletelyInvisibleChildClosestToEnd(var3, var4);
            }

            View var6;
            if (var2 == -1) {
               var6 = this.getChildClosestToStart();
            } else {
               var6 = this.getChildClosestToEnd();
            }

            if (var6.hasFocusable()) {
               return var1 == null ? null : var6;
            } else {
               return var1;
            }
         }
      }
   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      super.onInitializeAccessibilityEvent(var1);
      if (this.getChildCount() > 0) {
         var1.setFromIndex(this.findFirstVisibleItemPosition());
         var1.setToIndex(this.findLastVisibleItemPosition());
      }
   }

   public void onLayoutChildren(RecyclerView.Recycler var1, RecyclerView.State var2) {
      LinearLayoutManager.SavedState var8 = this.mPendingSavedState;
      byte var5 = -1;
      if ((var8 != null || this.mPendingScrollPosition != -1) && var2.getItemCount() == 0) {
         this.removeAndRecycleAllViews(var1);
      } else {
         var8 = this.mPendingSavedState;
         if (var8 != null && var8.hasValidAnchor()) {
            this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
         }

         this.ensureLayoutState();
         this.mLayoutState.mRecycle = false;
         this.resolveShouldLayoutReverse();
         View var10 = this.getFocusedChild();
         if (this.mAnchorInfo.mValid && this.mPendingScrollPosition == -1 && this.mPendingSavedState == null) {
            if (var10 != null && (this.mOrientationHelper.getDecoratedStart(var10) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(var10) <= this.mOrientationHelper.getStartAfterPadding())) {
               this.mAnchorInfo.assignFromViewAndKeepVisibleRect(var10);
            }
         } else {
            this.mAnchorInfo.reset();
            LinearLayoutManager.AnchorInfo var11 = this.mAnchorInfo;
            var11.mLayoutFromEnd = this.mShouldReverseLayout ^ this.mStackFromEnd;
            this.updateAnchorInfoForLayout(var1, var2, var11);
            this.mAnchorInfo.mValid = true;
         }

         int var3 = this.getExtraLayoutSpace(var2);
         int var4;
         if (this.mLayoutState.mLastScrollDelta >= 0) {
            var4 = 0;
         } else {
            var4 = var3;
            var3 = 0;
         }

         int var6;
         label96: {
            var4 += this.mOrientationHelper.getStartAfterPadding();
            var6 = var3 + this.mOrientationHelper.getEndPadding();
            if (var2.isPreLayout()) {
               var3 = this.mPendingScrollPosition;
               if (var3 != -1 && this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                  var10 = this.findViewByPosition(var3);
                  if (var10 != null) {
                     if (this.mShouldReverseLayout) {
                        var3 = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(var10) - this.mPendingScrollPositionOffset;
                     } else {
                        var3 = this.mOrientationHelper.getDecoratedStart(var10);
                        int var7 = this.mOrientationHelper.getStartAfterPadding();
                        var3 = this.mPendingScrollPositionOffset - (var3 - var7);
                     }

                     if (var3 > 0) {
                        var4 += var3;
                        var3 = var6;
                     } else {
                        var3 = var6 - var3;
                     }
                  } else {
                     var3 = var6;
                  }
                  break label96;
               }
            }

            var3 = var6;
         }

         if (this.mAnchorInfo.mLayoutFromEnd) {
            if (this.mShouldReverseLayout) {
               var5 = 1;
            }
         } else if (!this.mShouldReverseLayout) {
            var5 = 1;
         }

         this.onAnchorReady(var1, var2, this.mAnchorInfo, var5);
         this.detachAndScrapAttachedViews(var1);
         this.mLayoutState.mInfinite = this.resolveIsInfinite();
         this.mLayoutState.mIsPreLayout = var2.isPreLayout();
         int var9;
         LinearLayoutManager.LayoutState var12;
         if (this.mAnchorInfo.mLayoutFromEnd) {
            this.updateLayoutStateToFillStart(this.mAnchorInfo);
            var12 = this.mLayoutState;
            var12.mExtra = var4;
            this.fill(var1, var12, var2, false);
            var4 = this.mLayoutState.mOffset;
            var6 = this.mLayoutState.mCurrentPosition;
            if (this.mLayoutState.mAvailable > 0) {
               var3 += this.mLayoutState.mAvailable;
            }

            this.updateLayoutStateToFillEnd(this.mAnchorInfo);
            var12 = this.mLayoutState;
            var12.mExtra = var3;
            var12.mCurrentPosition += this.mLayoutState.mItemDirection;
            this.fill(var1, this.mLayoutState, var2, false);
            var9 = this.mLayoutState.mOffset;
            if (this.mLayoutState.mAvailable > 0) {
               var3 = this.mLayoutState.mAvailable;
               this.updateLayoutStateToFillStart(var6, var4);
               var12 = this.mLayoutState;
               var12.mExtra = var3;
               this.fill(var1, var12, var2, false);
               var3 = this.mLayoutState.mOffset;
            } else {
               var3 = var4;
            }

            var4 = var3;
            var3 = var9;
         } else {
            this.updateLayoutStateToFillEnd(this.mAnchorInfo);
            var12 = this.mLayoutState;
            var12.mExtra = var3;
            this.fill(var1, var12, var2, false);
            var3 = this.mLayoutState.mOffset;
            var9 = this.mLayoutState.mCurrentPosition;
            if (this.mLayoutState.mAvailable > 0) {
               var4 += this.mLayoutState.mAvailable;
            }

            this.updateLayoutStateToFillStart(this.mAnchorInfo);
            var12 = this.mLayoutState;
            var12.mExtra = var4;
            var12.mCurrentPosition += this.mLayoutState.mItemDirection;
            this.fill(var1, this.mLayoutState, var2, false);
            var4 = this.mLayoutState.mOffset;
            if (this.mLayoutState.mAvailable > 0) {
               var6 = this.mLayoutState.mAvailable;
               this.updateLayoutStateToFillEnd(var9, var3);
               var12 = this.mLayoutState;
               var12.mExtra = var6;
               this.fill(var1, var12, var2, false);
               var3 = this.mLayoutState.mOffset;
            }
         }

         if (this.getChildCount() > 0) {
            if (this.mShouldReverseLayout ^ this.mStackFromEnd) {
               var9 = this.fixLayoutEndGap(var3, var1, var2, true);
               var4 += var9;
               var6 = this.fixLayoutStartGap(var4, var1, var2, false);
               var4 += var6;
               var3 = var3 + var9 + var6;
            } else {
               var9 = this.fixLayoutStartGap(var4, var1, var2, true);
               var3 += var9;
               var6 = this.fixLayoutEndGap(var3, var1, var2, false);
               var4 = var4 + var9 + var6;
               var3 += var6;
            }
         }

         this.layoutForPredictiveAnimations(var1, var2, var4, var3);
         if (!var2.isPreLayout()) {
            this.mOrientationHelper.onLayoutComplete();
         } else {
            this.mAnchorInfo.reset();
         }

         this.mLastStackFromEnd = this.mStackFromEnd;
      }
   }

   public void onLayoutCompleted(RecyclerView.State var1) {
      super.onLayoutCompleted(var1);
      this.mPendingSavedState = null;
      this.mPendingScrollPosition = -1;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      this.mAnchorInfo.reset();
   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (var1 instanceof LinearLayoutManager.SavedState) {
         this.mPendingSavedState = (LinearLayoutManager.SavedState)var1;
         this.requestLayout();
      }
   }

   public Parcelable onSaveInstanceState() {
      LinearLayoutManager.SavedState var2 = this.mPendingSavedState;
      if (var2 != null) {
         return new LinearLayoutManager.SavedState(var2);
      } else {
         var2 = new LinearLayoutManager.SavedState();
         if (this.getChildCount() > 0) {
            this.ensureLayoutState();
            boolean var1 = this.mLastStackFromEnd ^ this.mShouldReverseLayout;
            var2.mAnchorLayoutFromEnd = var1;
            View var3;
            if (var1) {
               var3 = this.getChildClosestToEnd();
               var2.mAnchorOffset = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(var3);
               var2.mAnchorPosition = this.getPosition(var3);
            } else {
               var3 = this.getChildClosestToStart();
               var2.mAnchorPosition = this.getPosition(var3);
               var2.mAnchorOffset = this.mOrientationHelper.getDecoratedStart(var3) - this.mOrientationHelper.getStartAfterPadding();
            }

            return var2;
         } else {
            var2.invalidateAnchor();
            return var2;
         }
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void prepareForDrop(View var1, View var2, int var3, int var4) {
      this.assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
      this.ensureLayoutState();
      this.resolveShouldLayoutReverse();
      var3 = this.getPosition(var1);
      var4 = this.getPosition(var2);
      byte var5;
      if (var3 < var4) {
         var5 = 1;
      } else {
         var5 = -1;
      }

      if (this.mShouldReverseLayout) {
         if (var5 == 1) {
            this.scrollToPositionWithOffset(var4, this.mOrientationHelper.getEndAfterPadding() - (this.mOrientationHelper.getDecoratedStart(var2) + this.mOrientationHelper.getDecoratedMeasurement(var1)));
         } else {
            this.scrollToPositionWithOffset(var4, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(var2));
         }
      } else if (var5 == -1) {
         this.scrollToPositionWithOffset(var4, this.mOrientationHelper.getDecoratedStart(var2));
      } else {
         this.scrollToPositionWithOffset(var4, this.mOrientationHelper.getDecoratedEnd(var2) - this.mOrientationHelper.getDecoratedMeasurement(var1));
      }
   }

   boolean resolveIsInfinite() {
      return this.mOrientationHelper.getMode() == 0 && this.mOrientationHelper.getEnd() == 0;
   }

   int scrollBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      if (this.getChildCount() != 0) {
         if (var1 == 0) {
            return 0;
         } else {
            this.mLayoutState.mRecycle = true;
            this.ensureLayoutState();
            byte var4;
            if (var1 > 0) {
               var4 = 1;
            } else {
               var4 = -1;
            }

            int var5 = Math.abs(var1);
            this.updateLayoutState(var4, var5, true, var3);
            int var6 = this.mLayoutState.mScrollingOffset + this.fill(var2, this.mLayoutState, var3, false);
            if (var6 < 0) {
               return 0;
            } else {
               if (var5 > var6) {
                  var1 = var4 * var6;
               }

               this.mOrientationHelper.offsetChildren(-var1);
               this.mLayoutState.mLastScrollDelta = var1;
               return var1;
            }
         }
      } else {
         return 0;
      }
   }

   public int scrollHorizontallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      return this.mOrientation == 1 ? 0 : this.scrollBy(var1, var2, var3);
   }

   public void scrollToPosition(int var1) {
      this.mPendingScrollPosition = var1;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      LinearLayoutManager.SavedState var2 = this.mPendingSavedState;
      if (var2 != null) {
         var2.invalidateAnchor();
      }

      this.requestLayout();
   }

   public void scrollToPositionWithOffset(int var1, int var2) {
      this.mPendingScrollPosition = var1;
      this.mPendingScrollPositionOffset = var2;
      LinearLayoutManager.SavedState var3 = this.mPendingSavedState;
      if (var3 != null) {
         var3.invalidateAnchor();
      }

      this.requestLayout();
   }

   public int scrollVerticallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      return this.mOrientation == 0 ? 0 : this.scrollBy(var1, var2, var3);
   }

   public void setInitialPrefetchItemCount(int var1) {
      this.mInitialPrefetchItemCount = var1;
   }

   public void setOrientation(int var1) {
      if (var1 != 0 && var1 != 1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("invalid orientation:");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      } else {
         this.assertNotInLayoutOrScroll((String)null);
         if (var1 != this.mOrientation) {
            this.mOrientation = var1;
            this.mOrientationHelper = null;
            this.requestLayout();
         }
      }
   }

   public void setRecycleChildrenOnDetach(boolean var1) {
      this.mRecycleChildrenOnDetach = var1;
   }

   public void setReverseLayout(boolean var1) {
      this.assertNotInLayoutOrScroll((String)null);
      if (var1 != this.mReverseLayout) {
         this.mReverseLayout = var1;
         this.requestLayout();
      }
   }

   public void setSmoothScrollbarEnabled(boolean var1) {
      this.mSmoothScrollbarEnabled = var1;
   }

   public void setStackFromEnd(boolean var1) {
      this.assertNotInLayoutOrScroll((String)null);
      if (this.mStackFromEnd != var1) {
         this.mStackFromEnd = var1;
         this.requestLayout();
      }
   }

   boolean shouldMeasureTwice() {
      return this.getHeightMode() != 1073741824 && this.getWidthMode() != 1073741824 && this.hasFlexibleChildInBothOrientations();
   }

   public void smoothScrollToPosition(RecyclerView var1, RecyclerView.State var2, int var3) {
      LinearSmoothScroller var4 = new LinearSmoothScroller(var1.getContext());
      var4.setTargetPosition(var3);
      this.startSmoothScroll(var4);
   }

   public boolean supportsPredictiveItemAnimations() {
      return this.mPendingSavedState == null && this.mLastStackFromEnd == this.mStackFromEnd;
   }

   void validateChildOrder() {
      StringBuilder var8 = new StringBuilder();
      var8.append("validating child count ");
      var8.append(this.getChildCount());
      Log.d("LinearLayoutManager", var8.toString());
      if (this.getChildCount() >= 1) {
         boolean var7 = false;
         boolean var6 = false;
         int var2 = this.getPosition(this.getChildAt(0));
         int var3 = this.mOrientationHelper.getDecoratedStart(this.getChildAt(0));
         int var1;
         int var4;
         int var5;
         View var9;
         if (this.mShouldReverseLayout) {
            for(var1 = 1; var1 < this.getChildCount(); ++var1) {
               var9 = this.getChildAt(var1);
               var4 = this.getPosition(var9);
               var5 = this.mOrientationHelper.getDecoratedStart(var9);
               if (var4 < var2) {
                  this.logChildren();
                  var8 = new StringBuilder();
                  var8.append("detected invalid position. loc invalid? ");
                  if (var5 < var3) {
                     var6 = true;
                  }

                  var8.append(var6);
                  throw new RuntimeException(var8.toString());
               }

               if (var5 > var3) {
                  this.logChildren();
                  throw new RuntimeException("detected invalid location");
               }
            }

         } else {
            for(var1 = 1; var1 < this.getChildCount(); ++var1) {
               var9 = this.getChildAt(var1);
               var4 = this.getPosition(var9);
               var5 = this.mOrientationHelper.getDecoratedStart(var9);
               if (var4 < var2) {
                  this.logChildren();
                  var8 = new StringBuilder();
                  var8.append("detected invalid position. loc invalid? ");
                  var6 = var7;
                  if (var5 < var3) {
                     var6 = true;
                  }

                  var8.append(var6);
                  throw new RuntimeException(var8.toString());
               }

               if (var5 < var3) {
                  this.logChildren();
                  throw new RuntimeException("detected invalid location");
               }
            }

         }
      }
   }

   class AnchorInfo {
      int mCoordinate;
      boolean mLayoutFromEnd;
      int mPosition;
      boolean mValid;

      AnchorInfo() {
         this.reset();
      }

      void assignCoordinateFromPadding() {
         int var1;
         if (this.mLayoutFromEnd) {
            var1 = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding();
         } else {
            var1 = LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
         }

         this.mCoordinate = var1;
      }

      public void assignFromView(View var1) {
         if (this.mLayoutFromEnd) {
            this.mCoordinate = LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(var1) + LinearLayoutManager.this.mOrientationHelper.getTotalSpaceChange();
         } else {
            this.mCoordinate = LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(var1);
         }

         this.mPosition = LinearLayoutManager.this.getPosition(var1);
      }

      public void assignFromViewAndKeepVisibleRect(View var1) {
         int var3 = LinearLayoutManager.this.mOrientationHelper.getTotalSpaceChange();
         if (var3 >= 0) {
            this.assignFromView(var1);
         } else {
            this.mPosition = LinearLayoutManager.this.getPosition(var1);
            int var2;
            int var4;
            int var5;
            if (this.mLayoutFromEnd) {
               var2 = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - var3 - LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(var1);
               this.mCoordinate = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - var2;
               if (var2 > 0) {
                  var3 = LinearLayoutManager.this.mOrientationHelper.getDecoratedMeasurement(var1);
                  var4 = this.mCoordinate;
                  var5 = LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
                  var3 = var4 - var3 - (Math.min(LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(var1) - var5, 0) + var5);
                  if (var3 < 0) {
                     this.mCoordinate += Math.min(var2, -var3);
                  }
               }

            } else {
               var4 = LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(var1);
               var2 = var4 - LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
               this.mCoordinate = var4;
               if (var2 > 0) {
                  var5 = LinearLayoutManager.this.mOrientationHelper.getDecoratedMeasurement(var1);
                  int var6 = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding();
                  int var7 = LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(var1);
                  var3 = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - Math.min(0, var6 - var3 - var7) - (var5 + var4);
                  if (var3 < 0) {
                     this.mCoordinate -= Math.min(var2, -var3);
                  }
               }
            }
         }
      }

      boolean isViewValidAsAnchor(View var1, RecyclerView.State var2) {
         RecyclerView.LayoutParams var3 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         return !var3.isItemRemoved() && var3.getViewLayoutPosition() >= 0 && var3.getViewLayoutPosition() < var2.getItemCount();
      }

      void reset() {
         this.mPosition = -1;
         this.mCoordinate = Integer.MIN_VALUE;
         this.mLayoutFromEnd = false;
         this.mValid = false;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("AnchorInfo{mPosition=");
         var1.append(this.mPosition);
         var1.append(", mCoordinate=");
         var1.append(this.mCoordinate);
         var1.append(", mLayoutFromEnd=");
         var1.append(this.mLayoutFromEnd);
         var1.append(", mValid=");
         var1.append(this.mValid);
         var1.append('}');
         return var1.toString();
      }
   }

   protected static class LayoutChunkResult {
      public int mConsumed;
      public boolean mFinished;
      public boolean mFocusable;
      public boolean mIgnoreConsumed;

      void resetInternal() {
         this.mConsumed = 0;
         this.mFinished = false;
         this.mIgnoreConsumed = false;
         this.mFocusable = false;
      }
   }

   static class LayoutState {
      static final int INVALID_LAYOUT = Integer.MIN_VALUE;
      static final int ITEM_DIRECTION_HEAD = -1;
      static final int ITEM_DIRECTION_TAIL = 1;
      static final int LAYOUT_END = 1;
      static final int LAYOUT_START = -1;
      static final int SCROLLING_OFFSET_NaN = Integer.MIN_VALUE;
      static final String TAG = "LLM#LayoutState";
      int mAvailable;
      int mCurrentPosition;
      int mExtra = 0;
      boolean mInfinite;
      boolean mIsPreLayout = false;
      int mItemDirection;
      int mLastScrollDelta;
      int mLayoutDirection;
      int mOffset;
      boolean mRecycle = true;
      List mScrapList = null;
      int mScrollingOffset;

      private View nextViewFromScrapList() {
         int var2 = this.mScrapList.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            View var3 = ((RecyclerView.ViewHolder)this.mScrapList.get(var1)).itemView;
            RecyclerView.LayoutParams var4 = (RecyclerView.LayoutParams)var3.getLayoutParams();
            if (!var4.isItemRemoved() && this.mCurrentPosition == var4.getViewLayoutPosition()) {
               this.assignPositionFromScrapList(var3);
               return var3;
            }
         }

         return null;
      }

      public void assignPositionFromScrapList() {
         this.assignPositionFromScrapList((View)null);
      }

      public void assignPositionFromScrapList(View var1) {
         var1 = this.nextViewInLimitedList(var1);
         if (var1 == null) {
            this.mCurrentPosition = -1;
         } else {
            this.mCurrentPosition = ((RecyclerView.LayoutParams)var1.getLayoutParams()).getViewLayoutPosition();
         }
      }

      boolean hasMore(RecyclerView.State var1) {
         int var2 = this.mCurrentPosition;
         return var2 >= 0 && var2 < var1.getItemCount();
      }

      void log() {
         StringBuilder var1 = new StringBuilder();
         var1.append("avail:");
         var1.append(this.mAvailable);
         var1.append(", ind:");
         var1.append(this.mCurrentPosition);
         var1.append(", dir:");
         var1.append(this.mItemDirection);
         var1.append(", offset:");
         var1.append(this.mOffset);
         var1.append(", layoutDir:");
         var1.append(this.mLayoutDirection);
         Log.d("LLM#LayoutState", var1.toString());
      }

      View next(RecyclerView.Recycler var1) {
         if (this.mScrapList != null) {
            return this.nextViewFromScrapList();
         } else {
            View var2 = var1.getViewForPosition(this.mCurrentPosition);
            this.mCurrentPosition += this.mItemDirection;
            return var2;
         }
      }

      public View nextViewInLimitedList(View var1) {
         int var5 = this.mScrapList.size();
         View var6 = null;
         int var3 = Integer.MAX_VALUE;

         for(int var2 = 0; var2 < var5; ++var2) {
            View var7 = ((RecyclerView.ViewHolder)this.mScrapList.get(var2)).itemView;
            RecyclerView.LayoutParams var8 = (RecyclerView.LayoutParams)var7.getLayoutParams();
            if (var7 != var1 && !var8.isItemRemoved()) {
               int var4 = (var8.getViewLayoutPosition() - this.mCurrentPosition) * this.mItemDirection;
               if (var4 >= 0 && var4 < var3) {
                  var6 = var7;
                  var3 = var4;
                  if (var4 == 0) {
                     return var7;
                  }
               }
            }
         }

         return var6;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static class SavedState implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public LinearLayoutManager.SavedState createFromParcel(Parcel var1) {
            return new LinearLayoutManager.SavedState(var1);
         }

         public LinearLayoutManager.SavedState[] newArray(int var1) {
            return new LinearLayoutManager.SavedState[var1];
         }
      };
      boolean mAnchorLayoutFromEnd;
      int mAnchorOffset;
      int mAnchorPosition;

      public SavedState() {
      }

      SavedState(Parcel var1) {
         this.mAnchorPosition = var1.readInt();
         this.mAnchorOffset = var1.readInt();
         int var2 = var1.readInt();
         boolean var3 = true;
         if (var2 != 1) {
            var3 = false;
         }

         this.mAnchorLayoutFromEnd = var3;
      }

      public SavedState(LinearLayoutManager.SavedState var1) {
         this.mAnchorPosition = var1.mAnchorPosition;
         this.mAnchorOffset = var1.mAnchorOffset;
         this.mAnchorLayoutFromEnd = var1.mAnchorLayoutFromEnd;
      }

      public int describeContents() {
         return 0;
      }

      boolean hasValidAnchor() {
         return this.mAnchorPosition >= 0;
      }

      void invalidateAnchor() {
         this.mAnchorPosition = -1;
      }

      public void writeToParcel(Parcel var1, int var2) {
         throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
      }
   }
}
