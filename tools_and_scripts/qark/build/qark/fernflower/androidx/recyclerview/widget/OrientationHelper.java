package androidx.recyclerview.widget;

import android.graphics.Rect;
import android.view.View;

public abstract class OrientationHelper {
   public static final int HORIZONTAL = 0;
   private static final int INVALID_SIZE = Integer.MIN_VALUE;
   public static final int VERTICAL = 1;
   private int mLastTotalSpace;
   protected final RecyclerView.LayoutManager mLayoutManager;
   final Rect mTmpRect;

   private OrientationHelper(RecyclerView.LayoutManager var1) {
      this.mLastTotalSpace = Integer.MIN_VALUE;
      this.mTmpRect = new Rect();
      this.mLayoutManager = var1;
   }

   // $FF: synthetic method
   OrientationHelper(RecyclerView.LayoutManager var1, Object var2) {
      this(var1);
   }

   public static OrientationHelper createHorizontalHelper(RecyclerView.LayoutManager var0) {
      return new OrientationHelper(var0) {
         public int getDecoratedEnd(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return this.mLayoutManager.getDecoratedRight(var1) + var2.rightMargin;
         }

         public int getDecoratedMeasurement(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return this.mLayoutManager.getDecoratedMeasuredWidth(var1) + var2.leftMargin + var2.rightMargin;
         }

         public int getDecoratedMeasurementInOther(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return this.mLayoutManager.getDecoratedMeasuredHeight(var1) + var2.topMargin + var2.bottomMargin;
         }

         public int getDecoratedStart(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return this.mLayoutManager.getDecoratedLeft(var1) - var2.leftMargin;
         }

         public int getEnd() {
            return this.mLayoutManager.getWidth();
         }

         public int getEndAfterPadding() {
            return this.mLayoutManager.getWidth() - this.mLayoutManager.getPaddingRight();
         }

         public int getEndPadding() {
            return this.mLayoutManager.getPaddingRight();
         }

         public int getMode() {
            return this.mLayoutManager.getWidthMode();
         }

         public int getModeInOther() {
            return this.mLayoutManager.getHeightMode();
         }

         public int getStartAfterPadding() {
            return this.mLayoutManager.getPaddingLeft();
         }

         public int getTotalSpace() {
            return this.mLayoutManager.getWidth() - this.mLayoutManager.getPaddingLeft() - this.mLayoutManager.getPaddingRight();
         }

         public int getTransformedEndWithDecoration(View var1) {
            this.mLayoutManager.getTransformedBoundingBox(var1, true, this.mTmpRect);
            return this.mTmpRect.right;
         }

         public int getTransformedStartWithDecoration(View var1) {
            this.mLayoutManager.getTransformedBoundingBox(var1, true, this.mTmpRect);
            return this.mTmpRect.left;
         }

         public void offsetChild(View var1, int var2) {
            var1.offsetLeftAndRight(var2);
         }

         public void offsetChildren(int var1) {
            this.mLayoutManager.offsetChildrenHorizontal(var1);
         }
      };
   }

   public static OrientationHelper createOrientationHelper(RecyclerView.LayoutManager var0, int var1) {
      if (var1 != 0) {
         if (var1 == 1) {
            return createVerticalHelper(var0);
         } else {
            throw new IllegalArgumentException("invalid orientation");
         }
      } else {
         return createHorizontalHelper(var0);
      }
   }

   public static OrientationHelper createVerticalHelper(RecyclerView.LayoutManager var0) {
      return new OrientationHelper(var0) {
         public int getDecoratedEnd(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return this.mLayoutManager.getDecoratedBottom(var1) + var2.bottomMargin;
         }

         public int getDecoratedMeasurement(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return this.mLayoutManager.getDecoratedMeasuredHeight(var1) + var2.topMargin + var2.bottomMargin;
         }

         public int getDecoratedMeasurementInOther(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return this.mLayoutManager.getDecoratedMeasuredWidth(var1) + var2.leftMargin + var2.rightMargin;
         }

         public int getDecoratedStart(View var1) {
            RecyclerView.LayoutParams var2 = (RecyclerView.LayoutParams)var1.getLayoutParams();
            return this.mLayoutManager.getDecoratedTop(var1) - var2.topMargin;
         }

         public int getEnd() {
            return this.mLayoutManager.getHeight();
         }

         public int getEndAfterPadding() {
            return this.mLayoutManager.getHeight() - this.mLayoutManager.getPaddingBottom();
         }

         public int getEndPadding() {
            return this.mLayoutManager.getPaddingBottom();
         }

         public int getMode() {
            return this.mLayoutManager.getHeightMode();
         }

         public int getModeInOther() {
            return this.mLayoutManager.getWidthMode();
         }

         public int getStartAfterPadding() {
            return this.mLayoutManager.getPaddingTop();
         }

         public int getTotalSpace() {
            return this.mLayoutManager.getHeight() - this.mLayoutManager.getPaddingTop() - this.mLayoutManager.getPaddingBottom();
         }

         public int getTransformedEndWithDecoration(View var1) {
            this.mLayoutManager.getTransformedBoundingBox(var1, true, this.mTmpRect);
            return this.mTmpRect.bottom;
         }

         public int getTransformedStartWithDecoration(View var1) {
            this.mLayoutManager.getTransformedBoundingBox(var1, true, this.mTmpRect);
            return this.mTmpRect.top;
         }

         public void offsetChild(View var1, int var2) {
            var1.offsetTopAndBottom(var2);
         }

         public void offsetChildren(int var1) {
            this.mLayoutManager.offsetChildrenVertical(var1);
         }
      };
   }

   public abstract int getDecoratedEnd(View var1);

   public abstract int getDecoratedMeasurement(View var1);

   public abstract int getDecoratedMeasurementInOther(View var1);

   public abstract int getDecoratedStart(View var1);

   public abstract int getEnd();

   public abstract int getEndAfterPadding();

   public abstract int getEndPadding();

   public RecyclerView.LayoutManager getLayoutManager() {
      return this.mLayoutManager;
   }

   public abstract int getMode();

   public abstract int getModeInOther();

   public abstract int getStartAfterPadding();

   public abstract int getTotalSpace();

   public int getTotalSpaceChange() {
      return Integer.MIN_VALUE == this.mLastTotalSpace ? 0 : this.getTotalSpace() - this.mLastTotalSpace;
   }

   public abstract int getTransformedEndWithDecoration(View var1);

   public abstract int getTransformedStartWithDecoration(View var1);

   public abstract void offsetChild(View var1, int var2);

   public abstract void offsetChildren(int var1);

   public void onLayoutComplete() {
      this.mLastTotalSpace = this.getTotalSpace();
   }
}
