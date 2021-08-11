package androidx.viewpager2.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import androidx.recyclerview.widget.RecyclerView;

final class FakeDrag {
   private int mActualDraggedDistance;
   private long mFakeDragBeginTime;
   private int mMaximumVelocity;
   private final RecyclerView mRecyclerView;
   private float mRequestedDragDistance;
   private final ScrollEventAdapter mScrollEventAdapter;
   private VelocityTracker mVelocityTracker;
   private final ViewPager2 mViewPager;

   FakeDrag(ViewPager2 var1, ScrollEventAdapter var2, RecyclerView var3) {
      this.mViewPager = var1;
      this.mScrollEventAdapter = var2;
      this.mRecyclerView = var3;
   }

   private void addFakeMotionEvent(long var1, int var3, float var4, float var5) {
      MotionEvent var6 = MotionEvent.obtain(this.mFakeDragBeginTime, var1, var3, var4, var5, 0);
      this.mVelocityTracker.addMovement(var6);
      var6.recycle();
   }

   private void beginFakeVelocityTracker() {
      VelocityTracker var1 = this.mVelocityTracker;
      if (var1 == null) {
         this.mVelocityTracker = VelocityTracker.obtain();
         this.mMaximumVelocity = ViewConfiguration.get(this.mViewPager.getContext()).getScaledMaximumFlingVelocity();
      } else {
         var1.clear();
      }
   }

   boolean beginFakeDrag() {
      if (this.mScrollEventAdapter.isDragging()) {
         return false;
      } else {
         this.mActualDraggedDistance = 0;
         this.mRequestedDragDistance = (float)0;
         this.mFakeDragBeginTime = SystemClock.uptimeMillis();
         this.beginFakeVelocityTracker();
         this.mScrollEventAdapter.notifyBeginFakeDrag();
         if (!this.mScrollEventAdapter.isIdle()) {
            this.mRecyclerView.stopScroll();
         }

         this.addFakeMotionEvent(this.mFakeDragBeginTime, 0, 0.0F, 0.0F);
         return true;
      }
   }

   boolean endFakeDrag() {
      if (!this.mScrollEventAdapter.isFakeDragging()) {
         return false;
      } else {
         this.mScrollEventAdapter.notifyEndFakeDrag();
         VelocityTracker var3 = this.mVelocityTracker;
         var3.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
         int var1 = (int)var3.getXVelocity();
         int var2 = (int)var3.getYVelocity();
         if (!this.mRecyclerView.fling(var1, var2)) {
            this.mViewPager.snapToPage();
         }

         return true;
      }
   }

   boolean fakeDragBy(float var1) {
      boolean var7 = this.mScrollEventAdapter.isFakeDragging();
      byte var6 = 0;
      if (!var7) {
         return false;
      } else {
         var1 = this.mRequestedDragDistance - var1;
         this.mRequestedDragDistance = var1;
         int var3 = Math.round(var1 - (float)this.mActualDraggedDistance);
         this.mActualDraggedDistance += var3;
         long var8 = SystemClock.uptimeMillis();
         boolean var4;
         if (this.mViewPager.getOrientation() == 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         int var5;
         if (var4) {
            var5 = var3;
         } else {
            var5 = 0;
         }

         if (var4) {
            var3 = var6;
         }

         if (var4) {
            var1 = this.mRequestedDragDistance;
         } else {
            var1 = 0.0F;
         }

         float var2;
         if (var4) {
            var2 = 0.0F;
         } else {
            var2 = this.mRequestedDragDistance;
         }

         this.mRecyclerView.scrollBy(var5, var3);
         this.addFakeMotionEvent(var8, 2, var1, var2);
         return true;
      }
   }

   boolean isFakeDragging() {
      return this.mScrollEventAdapter.isFakeDragging();
   }
}
