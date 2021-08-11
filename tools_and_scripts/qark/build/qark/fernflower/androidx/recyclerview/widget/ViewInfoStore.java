package androidx.recyclerview.widget;

import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;
import androidx.core.util.Pools;

class ViewInfoStore {
   private static final boolean DEBUG = false;
   final SimpleArrayMap mLayoutHolderMap = new SimpleArrayMap();
   final LongSparseArray mOldChangedHolders = new LongSparseArray();

   private RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(RecyclerView.ViewHolder var1, int var2) {
      int var3 = this.mLayoutHolderMap.indexOfKey(var1);
      if (var3 < 0) {
         return null;
      } else {
         ViewInfoStore.InfoRecord var4 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.valueAt(var3);
         if (var4 != null && (var4.flags & var2) != 0) {
            var4.flags &= var2;
            RecyclerView.ItemAnimator.ItemHolderInfo var5;
            if (var2 == 4) {
               var5 = var4.preInfo;
            } else {
               if (var2 != 8) {
                  throw new IllegalArgumentException("Must provide flag PRE or POST");
               }

               var5 = var4.postInfo;
            }

            if ((var4.flags & 12) == 0) {
               this.mLayoutHolderMap.removeAt(var3);
               ViewInfoStore.InfoRecord.recycle(var4);
            }

            return var5;
         } else {
            return null;
         }
      }
   }

   void addToAppearedInPreLayoutHolders(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2) {
      ViewInfoStore.InfoRecord var4 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      ViewInfoStore.InfoRecord var3 = var4;
      if (var4 == null) {
         var3 = ViewInfoStore.InfoRecord.obtain();
         this.mLayoutHolderMap.put(var1, var3);
      }

      var3.flags |= 2;
      var3.preInfo = var2;
   }

   void addToDisappearedInLayout(RecyclerView.ViewHolder var1) {
      ViewInfoStore.InfoRecord var3 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      ViewInfoStore.InfoRecord var2 = var3;
      if (var3 == null) {
         var2 = ViewInfoStore.InfoRecord.obtain();
         this.mLayoutHolderMap.put(var1, var2);
      }

      var2.flags |= 1;
   }

   void addToOldChangeHolders(long var1, RecyclerView.ViewHolder var3) {
      this.mOldChangedHolders.put(var1, var3);
   }

   void addToPostLayout(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2) {
      ViewInfoStore.InfoRecord var4 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      ViewInfoStore.InfoRecord var3 = var4;
      if (var4 == null) {
         var3 = ViewInfoStore.InfoRecord.obtain();
         this.mLayoutHolderMap.put(var1, var3);
      }

      var3.postInfo = var2;
      var3.flags |= 8;
   }

   void addToPreLayout(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2) {
      ViewInfoStore.InfoRecord var4 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      ViewInfoStore.InfoRecord var3 = var4;
      if (var4 == null) {
         var3 = ViewInfoStore.InfoRecord.obtain();
         this.mLayoutHolderMap.put(var1, var3);
      }

      var3.preInfo = var2;
      var3.flags |= 4;
   }

   void clear() {
      this.mLayoutHolderMap.clear();
      this.mOldChangedHolders.clear();
   }

   RecyclerView.ViewHolder getFromOldChangeHolders(long var1) {
      return (RecyclerView.ViewHolder)this.mOldChangedHolders.get(var1);
   }

   boolean isDisappearing(RecyclerView.ViewHolder var1) {
      ViewInfoStore.InfoRecord var2 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      return var2 != null && (var2.flags & 1) != 0;
   }

   boolean isInPreLayout(RecyclerView.ViewHolder var1) {
      ViewInfoStore.InfoRecord var2 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      return var2 != null && (var2.flags & 4) != 0;
   }

   void onDetach() {
      ViewInfoStore.InfoRecord.drainCache();
   }

   public void onViewDetached(RecyclerView.ViewHolder var1) {
      this.removeFromDisappearedInLayout(var1);
   }

   RecyclerView.ItemAnimator.ItemHolderInfo popFromPostLayout(RecyclerView.ViewHolder var1) {
      return this.popFromLayoutStep(var1, 8);
   }

   RecyclerView.ItemAnimator.ItemHolderInfo popFromPreLayout(RecyclerView.ViewHolder var1) {
      return this.popFromLayoutStep(var1, 4);
   }

   void process(ViewInfoStore.ProcessCallback var1) {
      for(int var2 = this.mLayoutHolderMap.size() - 1; var2 >= 0; --var2) {
         RecyclerView.ViewHolder var4 = (RecyclerView.ViewHolder)this.mLayoutHolderMap.keyAt(var2);
         ViewInfoStore.InfoRecord var5 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.removeAt(var2);
         if ((var5.flags & 3) == 3) {
            var1.unused(var4);
         } else if ((var5.flags & 1) != 0) {
            if (var5.preInfo == null) {
               var1.unused(var4);
            } else {
               var1.processDisappeared(var4, var5.preInfo, var5.postInfo);
            }
         } else if ((var5.flags & 14) == 14) {
            var1.processAppeared(var4, var5.preInfo, var5.postInfo);
         } else if ((var5.flags & 12) == 12) {
            var1.processPersistent(var4, var5.preInfo, var5.postInfo);
         } else if ((var5.flags & 4) != 0) {
            var1.processDisappeared(var4, var5.preInfo, (RecyclerView.ItemAnimator.ItemHolderInfo)null);
         } else if ((var5.flags & 8) != 0) {
            var1.processAppeared(var4, var5.preInfo, var5.postInfo);
         } else {
            int var3 = var5.flags;
         }

         ViewInfoStore.InfoRecord.recycle(var5);
      }

   }

   void removeFromDisappearedInLayout(RecyclerView.ViewHolder var1) {
      ViewInfoStore.InfoRecord var2 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      if (var2 != null) {
         var2.flags &= -2;
      }
   }

   void removeViewHolder(RecyclerView.ViewHolder var1) {
      for(int var2 = this.mOldChangedHolders.size() - 1; var2 >= 0; --var2) {
         if (var1 == this.mOldChangedHolders.valueAt(var2)) {
            this.mOldChangedHolders.removeAt(var2);
            break;
         }
      }

      ViewInfoStore.InfoRecord var3 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.remove(var1);
      if (var3 != null) {
         ViewInfoStore.InfoRecord.recycle(var3);
      }

   }

   static class InfoRecord {
      static final int FLAG_APPEAR = 2;
      static final int FLAG_APPEAR_AND_DISAPPEAR = 3;
      static final int FLAG_APPEAR_PRE_AND_POST = 14;
      static final int FLAG_DISAPPEARED = 1;
      static final int FLAG_POST = 8;
      static final int FLAG_PRE = 4;
      static final int FLAG_PRE_AND_POST = 12;
      static Pools.Pool sPool = new Pools.SimplePool(20);
      int flags;
      RecyclerView.ItemAnimator.ItemHolderInfo postInfo;
      RecyclerView.ItemAnimator.ItemHolderInfo preInfo;

      private InfoRecord() {
      }

      static void drainCache() {
         while(sPool.acquire() != null) {
         }

      }

      static ViewInfoStore.InfoRecord obtain() {
         ViewInfoStore.InfoRecord var0 = (ViewInfoStore.InfoRecord)sPool.acquire();
         return var0 == null ? new ViewInfoStore.InfoRecord() : var0;
      }

      static void recycle(ViewInfoStore.InfoRecord var0) {
         var0.flags = 0;
         var0.preInfo = null;
         var0.postInfo = null;
         sPool.release(var0);
      }
   }

   interface ProcessCallback {
      void processAppeared(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      void processDisappeared(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      void processPersistent(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      void unused(RecyclerView.ViewHolder var1);
   }
}
