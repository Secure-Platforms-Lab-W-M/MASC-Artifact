package androidx.recyclerview.widget;

import android.view.View;

class LayoutState {
   static final int INVALID_LAYOUT = Integer.MIN_VALUE;
   static final int ITEM_DIRECTION_HEAD = -1;
   static final int ITEM_DIRECTION_TAIL = 1;
   static final int LAYOUT_END = 1;
   static final int LAYOUT_START = -1;
   int mAvailable;
   int mCurrentPosition;
   int mEndLine = 0;
   boolean mInfinite;
   int mItemDirection;
   int mLayoutDirection;
   boolean mRecycle = true;
   int mStartLine = 0;
   boolean mStopInFocusable;

   boolean hasMore(RecyclerView.State var1) {
      int var2 = this.mCurrentPosition;
      return var2 >= 0 && var2 < var1.getItemCount();
   }

   View next(RecyclerView.Recycler var1) {
      View var2 = var1.getViewForPosition(this.mCurrentPosition);
      this.mCurrentPosition += this.mItemDirection;
      return var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("LayoutState{mAvailable=");
      var1.append(this.mAvailable);
      var1.append(", mCurrentPosition=");
      var1.append(this.mCurrentPosition);
      var1.append(", mItemDirection=");
      var1.append(this.mItemDirection);
      var1.append(", mLayoutDirection=");
      var1.append(this.mLayoutDirection);
      var1.append(", mStartLine=");
      var1.append(this.mStartLine);
      var1.append(", mEndLine=");
      var1.append(this.mEndLine);
      var1.append('}');
      return var1.toString();
   }
}
