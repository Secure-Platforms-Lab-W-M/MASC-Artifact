package android.support.v7.util;

import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

public class AsyncListUtil {
   static final boolean DEBUG = false;
   static final String TAG = "AsyncListUtil";
   boolean mAllowScrollHints;
   private final ThreadUtil.BackgroundCallback mBackgroundCallback;
   final ThreadUtil.BackgroundCallback mBackgroundProxy;
   final AsyncListUtil.DataCallback mDataCallback;
   int mDisplayedGeneration = 0;
   int mItemCount = 0;
   private final ThreadUtil.MainThreadCallback mMainThreadCallback;
   final ThreadUtil.MainThreadCallback mMainThreadProxy;
   final SparseIntArray mMissingPositions;
   final int[] mPrevRange = new int[2];
   int mRequestedGeneration;
   private int mScrollHint = 0;
   final Class mTClass;
   final TileList mTileList;
   final int mTileSize;
   final int[] mTmpRange = new int[2];
   final int[] mTmpRangeExtended = new int[2];
   final AsyncListUtil.ViewCallback mViewCallback;

   public AsyncListUtil(Class var1, int var2, AsyncListUtil.DataCallback var3, AsyncListUtil.ViewCallback var4) {
      this.mRequestedGeneration = this.mDisplayedGeneration;
      this.mMissingPositions = new SparseIntArray();
      this.mMainThreadCallback = new ThreadUtil.MainThreadCallback() {
         private boolean isRequestedGeneration(int var1) {
            return var1 == AsyncListUtil.this.mRequestedGeneration;
         }

         private void recycleAllTiles() {
            for(int var1 = 0; var1 < AsyncListUtil.this.mTileList.size(); ++var1) {
               AsyncListUtil.this.mBackgroundProxy.recycleTile(AsyncListUtil.this.mTileList.getAtIndex(var1));
            }

            AsyncListUtil.this.mTileList.clear();
         }

         public void addTile(int var1, TileList.Tile var2) {
            if (!this.isRequestedGeneration(var1)) {
               AsyncListUtil.this.mBackgroundProxy.recycleTile(var2);
            } else {
               TileList.Tile var6 = AsyncListUtil.this.mTileList.addOrReplace(var2);
               if (var6 != null) {
                  StringBuilder var7 = new StringBuilder();
                  var7.append("duplicate tile @");
                  var7.append(var6.mStartPosition);
                  Log.e("AsyncListUtil", var7.toString());
                  AsyncListUtil.this.mBackgroundProxy.recycleTile(var6);
               }

               int var3 = var2.mStartPosition;
               int var4 = var2.mItemCount;
               var1 = 0;

               while(true) {
                  while(var1 < AsyncListUtil.this.mMissingPositions.size()) {
                     int var5 = AsyncListUtil.this.mMissingPositions.keyAt(var1);
                     if (var2.mStartPosition <= var5 && var5 < var3 + var4) {
                        AsyncListUtil.this.mMissingPositions.removeAt(var1);
                        AsyncListUtil.this.mViewCallback.onItemLoaded(var5);
                     } else {
                        ++var1;
                     }
                  }

                  return;
               }
            }
         }

         public void removeTile(int var1, int var2) {
            if (this.isRequestedGeneration(var1)) {
               TileList.Tile var3 = AsyncListUtil.this.mTileList.removeAtPos(var2);
               if (var3 == null) {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("tile not found @");
                  var4.append(var2);
                  Log.e("AsyncListUtil", var4.toString());
               } else {
                  AsyncListUtil.this.mBackgroundProxy.recycleTile(var3);
               }
            }
         }

         public void updateItemCount(int var1, int var2) {
            if (this.isRequestedGeneration(var1)) {
               AsyncListUtil var3 = AsyncListUtil.this;
               var3.mItemCount = var2;
               var3.mViewCallback.onDataRefresh();
               var3 = AsyncListUtil.this;
               var3.mDisplayedGeneration = var3.mRequestedGeneration;
               this.recycleAllTiles();
               var3 = AsyncListUtil.this;
               var3.mAllowScrollHints = false;
               var3.updateRange();
            }
         }
      };
      this.mBackgroundCallback = new ThreadUtil.BackgroundCallback() {
         private int mFirstRequiredTileStart;
         private int mGeneration;
         private int mItemCount;
         private int mLastRequiredTileStart;
         final SparseBooleanArray mLoadedTiles = new SparseBooleanArray();
         private TileList.Tile mRecycledRoot;

         private TileList.Tile acquireTile() {
            TileList.Tile var1 = this.mRecycledRoot;
            if (var1 != null) {
               TileList.Tile var2 = this.mRecycledRoot;
               this.mRecycledRoot = var1.mNext;
               return var2;
            } else {
               return new TileList.Tile(AsyncListUtil.this.mTClass, AsyncListUtil.this.mTileSize);
            }
         }

         private void addTile(TileList.Tile var1) {
            this.mLoadedTiles.put(var1.mStartPosition, true);
            AsyncListUtil.this.mMainThreadProxy.addTile(this.mGeneration, var1);
         }

         private void flushTileCache(int var1) {
            int var2 = AsyncListUtil.this.mDataCallback.getMaxCachedTiles();

            while(true) {
               while(this.mLoadedTiles.size() >= var2) {
                  int var3 = this.mLoadedTiles.keyAt(0);
                  SparseBooleanArray var7 = this.mLoadedTiles;
                  int var4 = var7.keyAt(var7.size() - 1);
                  int var5 = this.mFirstRequiredTileStart - var3;
                  int var6 = var4 - this.mLastRequiredTileStart;
                  if (var5 <= 0 || var5 < var6 && var1 != 2) {
                     if (var6 <= 0 || var5 >= var6 && var1 != 1) {
                        return;
                     }

                     this.removeTile(var4);
                  } else {
                     this.removeTile(var3);
                  }
               }

               return;
            }
         }

         private int getTileStart(int var1) {
            return var1 - var1 % AsyncListUtil.this.mTileSize;
         }

         private boolean isTileLoaded(int var1) {
            return this.mLoadedTiles.get(var1);
         }

         private void log(String var1, Object... var2) {
            StringBuilder var3 = new StringBuilder();
            var3.append("[BKGR] ");
            var3.append(String.format(var1, var2));
            Log.d("AsyncListUtil", var3.toString());
         }

         private void removeTile(int var1) {
            this.mLoadedTiles.delete(var1);
            AsyncListUtil.this.mMainThreadProxy.removeTile(this.mGeneration, var1);
         }

         private void requestTiles(int var1, int var2, int var3, boolean var4) {
            for(int var5 = var1; var5 <= var2; var5 += AsyncListUtil.this.mTileSize) {
               int var6;
               if (var4) {
                  var6 = var2 + var1 - var5;
               } else {
                  var6 = var5;
               }

               AsyncListUtil.this.mBackgroundProxy.loadTile(var6, var3);
            }

         }

         public void loadTile(int var1, int var2) {
            if (!this.isTileLoaded(var1)) {
               TileList.Tile var3 = this.acquireTile();
               var3.mStartPosition = var1;
               var3.mItemCount = Math.min(AsyncListUtil.this.mTileSize, this.mItemCount - var3.mStartPosition);
               AsyncListUtil.this.mDataCallback.fillData(var3.mItems, var3.mStartPosition, var3.mItemCount);
               this.flushTileCache(var2);
               this.addTile(var3);
            }
         }

         public void recycleTile(TileList.Tile var1) {
            AsyncListUtil.this.mDataCallback.recycleData(var1.mItems, var1.mItemCount);
            var1.mNext = this.mRecycledRoot;
            this.mRecycledRoot = var1;
         }

         public void refresh(int var1) {
            this.mGeneration = var1;
            this.mLoadedTiles.clear();
            this.mItemCount = AsyncListUtil.this.mDataCallback.refreshData();
            AsyncListUtil.this.mMainThreadProxy.updateItemCount(this.mGeneration, this.mItemCount);
         }

         public void updateRange(int var1, int var2, int var3, int var4, int var5) {
            if (var1 <= var2) {
               var1 = this.getTileStart(var1);
               var2 = this.getTileStart(var2);
               this.mFirstRequiredTileStart = this.getTileStart(var3);
               this.mLastRequiredTileStart = this.getTileStart(var4);
               if (var5 == 1) {
                  this.requestTiles(this.mFirstRequiredTileStart, var2, var5, true);
                  this.requestTiles(AsyncListUtil.this.mTileSize + var2, this.mLastRequiredTileStart, var5, false);
               } else {
                  this.requestTiles(var1, this.mLastRequiredTileStart, var5, false);
                  this.requestTiles(this.mFirstRequiredTileStart, var1 - AsyncListUtil.this.mTileSize, var5, true);
               }
            }
         }
      };
      this.mTClass = var1;
      this.mTileSize = var2;
      this.mDataCallback = var3;
      this.mViewCallback = var4;
      this.mTileList = new TileList(this.mTileSize);
      MessageThreadUtil var5 = new MessageThreadUtil();
      this.mMainThreadProxy = var5.getMainThreadProxy(this.mMainThreadCallback);
      this.mBackgroundProxy = var5.getBackgroundProxy(this.mBackgroundCallback);
      this.refresh();
   }

   private boolean isRefreshPending() {
      return this.mRequestedGeneration != this.mDisplayedGeneration;
   }

   public Object getItem(int var1) {
      if (var1 >= 0 && var1 < this.mItemCount) {
         Object var3 = this.mTileList.getItemAt(var1);
         if (var3 == null && !this.isRefreshPending()) {
            this.mMissingPositions.put(var1, 0);
            return var3;
         } else {
            return var3;
         }
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append(var1);
         var2.append(" is not within 0 and ");
         var2.append(this.mItemCount);
         throw new IndexOutOfBoundsException(var2.toString());
      }
   }

   public int getItemCount() {
      return this.mItemCount;
   }

   void log(String var1, Object... var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("[MAIN] ");
      var3.append(String.format(var1, var2));
      Log.d("AsyncListUtil", var3.toString());
   }

   public void onRangeChanged() {
      if (!this.isRefreshPending()) {
         this.updateRange();
         this.mAllowScrollHints = true;
      }
   }

   public void refresh() {
      this.mMissingPositions.clear();
      ThreadUtil.BackgroundCallback var2 = this.mBackgroundProxy;
      int var1 = this.mRequestedGeneration + 1;
      this.mRequestedGeneration = var1;
      var2.refresh(var1);
   }

   void updateRange() {
      this.mViewCallback.getItemRangeInto(this.mTmpRange);
      int[] var3 = this.mTmpRange;
      if (var3[0] <= var3[1]) {
         if (var3[0] >= 0) {
            if (var3[1] < this.mItemCount) {
               int var1;
               int[] var4;
               if (!this.mAllowScrollHints) {
                  this.mScrollHint = 0;
               } else {
                  var1 = var3[0];
                  var4 = this.mPrevRange;
                  if (var1 <= var4[1] && var4[0] <= var3[1]) {
                     if (var3[0] < var4[0]) {
                        this.mScrollHint = 1;
                     } else if (var3[0] > var4[0]) {
                        this.mScrollHint = 2;
                     }
                  } else {
                     this.mScrollHint = 0;
                  }
               }

               var3 = this.mPrevRange;
               var4 = this.mTmpRange;
               var3[0] = var4[0];
               var3[1] = var4[1];
               this.mViewCallback.extendRangeInto(var4, this.mTmpRangeExtended, this.mScrollHint);
               var3 = this.mTmpRangeExtended;
               var3[0] = Math.min(this.mTmpRange[0], Math.max(var3[0], 0));
               var3 = this.mTmpRangeExtended;
               var3[1] = Math.max(this.mTmpRange[1], Math.min(var3[1], this.mItemCount - 1));
               ThreadUtil.BackgroundCallback var5 = this.mBackgroundProxy;
               var4 = this.mTmpRange;
               var1 = var4[0];
               int var2 = var4[1];
               var4 = this.mTmpRangeExtended;
               var5.updateRange(var1, var2, var4[0], var4[1], this.mScrollHint);
            }
         }
      }
   }

   public abstract static class DataCallback {
      @WorkerThread
      public abstract void fillData(Object[] var1, int var2, int var3);

      @WorkerThread
      public int getMaxCachedTiles() {
         return 10;
      }

      @WorkerThread
      public void recycleData(Object[] var1, int var2) {
      }

      @WorkerThread
      public abstract int refreshData();
   }

   public abstract static class ViewCallback {
      public static final int HINT_SCROLL_ASC = 2;
      public static final int HINT_SCROLL_DESC = 1;
      public static final int HINT_SCROLL_NONE = 0;

      @UiThread
      public void extendRangeInto(int[] var1, int[] var2, int var3) {
         int var4 = var1[1] - var1[0] + 1;
         int var5 = var4 / 2;
         int var7 = var1[0];
         int var6;
         if (var3 == 1) {
            var6 = var4;
         } else {
            var6 = var5;
         }

         var2[0] = var7 - var6;
         var6 = var1[1];
         if (var3 != 2) {
            var4 = var5;
         }

         var2[1] = var6 + var4;
      }

      @UiThread
      public abstract void getItemRangeInto(int[] var1);

      @UiThread
      public abstract void onDataRefresh();

      @UiThread
      public abstract void onItemLoaded(int var1);
   }
}
