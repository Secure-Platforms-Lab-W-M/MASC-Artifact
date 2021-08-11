package android.support.v7.widget;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import java.util.ArrayList;
import java.util.List;

class ChildHelper {
   private static final boolean DEBUG = false;
   private static final String TAG = "ChildrenHelper";
   final ChildHelper.Bucket mBucket;
   final ChildHelper.Callback mCallback;
   final List mHiddenViews;

   ChildHelper(ChildHelper.Callback var1) {
      this.mCallback = var1;
      this.mBucket = new ChildHelper.Bucket();
      this.mHiddenViews = new ArrayList();
   }

   private int getOffset(int var1) {
      if (var1 < 0) {
         return -1;
      } else {
         int var3 = this.mCallback.getChildCount();

         int var4;
         for(int var2 = var1; var2 < var3; var2 += var4) {
            var4 = var1 - (var2 - this.mBucket.countOnesBefore(var2));
            if (var4 == 0) {
               while(this.mBucket.get(var2)) {
                  ++var2;
               }

               return var2;
            }
         }

         return -1;
      }
   }

   private void hideViewInternal(View var1) {
      this.mHiddenViews.add(var1);
      this.mCallback.onEnteredHiddenState(var1);
   }

   private boolean unhideViewInternal(View var1) {
      if (this.mHiddenViews.remove(var1)) {
         this.mCallback.onLeftHiddenState(var1);
         return true;
      } else {
         return false;
      }
   }

   void addView(View var1, int var2, boolean var3) {
      if (var2 < 0) {
         var2 = this.mCallback.getChildCount();
      } else {
         var2 = this.getOffset(var2);
      }

      this.mBucket.insert(var2, var3);
      if (var3) {
         this.hideViewInternal(var1);
      }

      this.mCallback.addView(var1, var2);
   }

   void addView(View var1, boolean var2) {
      this.addView(var1, -1, var2);
   }

   void attachViewToParent(View var1, int var2, LayoutParams var3, boolean var4) {
      if (var2 < 0) {
         var2 = this.mCallback.getChildCount();
      } else {
         var2 = this.getOffset(var2);
      }

      this.mBucket.insert(var2, var4);
      if (var4) {
         this.hideViewInternal(var1);
      }

      this.mCallback.attachViewToParent(var1, var2, var3);
   }

   void detachViewFromParent(int var1) {
      var1 = this.getOffset(var1);
      this.mBucket.remove(var1);
      this.mCallback.detachViewFromParent(var1);
   }

   View findHiddenNonRemovedView(int var1) {
      int var3 = this.mHiddenViews.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         View var4 = (View)this.mHiddenViews.get(var2);
         RecyclerView.ViewHolder var5 = this.mCallback.getChildViewHolder(var4);
         if (var5.getLayoutPosition() == var1 && !var5.isInvalid() && !var5.isRemoved()) {
            return var4;
         }
      }

      return null;
   }

   View getChildAt(int var1) {
      var1 = this.getOffset(var1);
      return this.mCallback.getChildAt(var1);
   }

   int getChildCount() {
      return this.mCallback.getChildCount() - this.mHiddenViews.size();
   }

   View getUnfilteredChildAt(int var1) {
      return this.mCallback.getChildAt(var1);
   }

   int getUnfilteredChildCount() {
      return this.mCallback.getChildCount();
   }

   void hide(View var1) {
      int var2 = this.mCallback.indexOfChild(var1);
      if (var2 >= 0) {
         this.mBucket.set(var2);
         this.hideViewInternal(var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("view is not a child, cannot hide ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   int indexOfChild(View var1) {
      int var2 = this.mCallback.indexOfChild(var1);
      if (var2 == -1) {
         return -1;
      } else {
         return this.mBucket.get(var2) ? -1 : var2 - this.mBucket.countOnesBefore(var2);
      }
   }

   boolean isHidden(View var1) {
      return this.mHiddenViews.contains(var1);
   }

   void removeAllViewsUnfiltered() {
      this.mBucket.reset();

      for(int var1 = this.mHiddenViews.size() - 1; var1 >= 0; --var1) {
         this.mCallback.onLeftHiddenState((View)this.mHiddenViews.get(var1));
         this.mHiddenViews.remove(var1);
      }

      this.mCallback.removeAllViews();
   }

   void removeView(View var1) {
      int var2 = this.mCallback.indexOfChild(var1);
      if (var2 >= 0) {
         if (this.mBucket.remove(var2)) {
            this.unhideViewInternal(var1);
         }

         this.mCallback.removeViewAt(var2);
      }
   }

   void removeViewAt(int var1) {
      var1 = this.getOffset(var1);
      View var2 = this.mCallback.getChildAt(var1);
      if (var2 != null) {
         if (this.mBucket.remove(var1)) {
            this.unhideViewInternal(var2);
         }

         this.mCallback.removeViewAt(var1);
      }
   }

   boolean removeViewIfHidden(View var1) {
      int var2 = this.mCallback.indexOfChild(var1);
      if (var2 == -1) {
         this.unhideViewInternal(var1);
         return true;
      } else if (this.mBucket.get(var2)) {
         this.mBucket.remove(var2);
         this.unhideViewInternal(var1);
         this.mCallback.removeViewAt(var2);
         return true;
      } else {
         return false;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.mBucket.toString());
      var1.append(", hidden list:");
      var1.append(this.mHiddenViews.size());
      return var1.toString();
   }

   void unhide(View var1) {
      int var2 = this.mCallback.indexOfChild(var1);
      StringBuilder var3;
      if (var2 >= 0) {
         if (this.mBucket.get(var2)) {
            this.mBucket.clear(var2);
            this.unhideViewInternal(var1);
         } else {
            var3 = new StringBuilder();
            var3.append("trying to unhide a view that was not hidden");
            var3.append(var1);
            throw new RuntimeException(var3.toString());
         }
      } else {
         var3 = new StringBuilder();
         var3.append("view is not a child, cannot hide ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   static class Bucket {
      static final int BITS_PER_WORD = 64;
      static final long LAST_BIT = Long.MIN_VALUE;
      long mData = 0L;
      ChildHelper.Bucket mNext;

      private void ensureNext() {
         if (this.mNext == null) {
            this.mNext = new ChildHelper.Bucket();
         }
      }

      void clear(int var1) {
         if (var1 >= 64) {
            ChildHelper.Bucket var2 = this.mNext;
            if (var2 != null) {
               var2.clear(var1 - 64);
            }
         } else {
            this.mData &= ~(1L << var1);
         }
      }

      int countOnesBefore(int var1) {
         ChildHelper.Bucket var2 = this.mNext;
         if (var2 == null) {
            return var1 >= 64 ? Long.bitCount(this.mData) : Long.bitCount(this.mData & (1L << var1) - 1L);
         } else {
            return var1 < 64 ? Long.bitCount(this.mData & (1L << var1) - 1L) : var2.countOnesBefore(var1 - 64) + Long.bitCount(this.mData);
         }
      }

      boolean get(int var1) {
         if (var1 >= 64) {
            this.ensureNext();
            return this.mNext.get(var1 - 64);
         } else {
            return (this.mData & 1L << var1) != 0L;
         }
      }

      void insert(int var1, boolean var2) {
         if (var1 >= 64) {
            this.ensureNext();
            this.mNext.insert(var1 - 64, var2);
         } else {
            boolean var3;
            if ((this.mData & Long.MIN_VALUE) != 0L) {
               var3 = true;
            } else {
               var3 = false;
            }

            long var4 = (1L << var1) - 1L;
            long var6 = this.mData;
            this.mData = var6 & var4 | (var6 & ~var4) << 1;
            if (var2) {
               this.set(var1);
            } else {
               this.clear(var1);
            }

            if (var3 || this.mNext != null) {
               this.ensureNext();
               this.mNext.insert(0, var3);
            }
         }
      }

      boolean remove(int var1) {
         if (var1 >= 64) {
            this.ensureNext();
            return this.mNext.remove(var1 - 64);
         } else {
            long var2 = 1L << var1;
            boolean var6;
            if ((this.mData & var2) != 0L) {
               var6 = true;
            } else {
               var6 = false;
            }

            this.mData &= ~var2;
            --var2;
            long var4 = this.mData;
            this.mData = var4 & var2 | Long.rotateRight(var4 & ~var2, 1);
            ChildHelper.Bucket var7 = this.mNext;
            if (var7 != null) {
               if (var7.get(0)) {
                  this.set(63);
               }

               this.mNext.remove(0);
               return var6;
            } else {
               return var6;
            }
         }
      }

      void reset() {
         this.mData = 0L;
         ChildHelper.Bucket var1 = this.mNext;
         if (var1 != null) {
            var1.reset();
         }
      }

      void set(int var1) {
         if (var1 >= 64) {
            this.ensureNext();
            this.mNext.set(var1 - 64);
         } else {
            this.mData |= 1L << var1;
         }
      }

      public String toString() {
         if (this.mNext == null) {
            return Long.toBinaryString(this.mData);
         } else {
            StringBuilder var1 = new StringBuilder();
            var1.append(this.mNext.toString());
            var1.append("xx");
            var1.append(Long.toBinaryString(this.mData));
            return var1.toString();
         }
      }
   }

   interface Callback {
      void addView(View var1, int var2);

      void attachViewToParent(View var1, int var2, LayoutParams var3);

      void detachViewFromParent(int var1);

      View getChildAt(int var1);

      int getChildCount();

      RecyclerView.ViewHolder getChildViewHolder(View var1);

      int indexOfChild(View var1);

      void onEnteredHiddenState(View var1);

      void onLeftHiddenState(View var1);

      void removeAllViews();

      void removeViewAt(int var1);
   }
}
