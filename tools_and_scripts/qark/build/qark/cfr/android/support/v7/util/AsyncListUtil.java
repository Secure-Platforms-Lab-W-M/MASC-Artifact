/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.util.SparseBooleanArray
 *  android.util.SparseIntArray
 */
package android.support.v7.util;

import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.support.v7.util.MessageThreadUtil;
import android.support.v7.util.ThreadUtil;
import android.support.v7.util.TileList;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

public class AsyncListUtil<T> {
    static final boolean DEBUG = false;
    static final String TAG = "AsyncListUtil";
    boolean mAllowScrollHints;
    private final ThreadUtil.BackgroundCallback<T> mBackgroundCallback;
    final ThreadUtil.BackgroundCallback<T> mBackgroundProxy;
    final DataCallback<T> mDataCallback;
    int mDisplayedGeneration;
    int mItemCount = 0;
    private final ThreadUtil.MainThreadCallback<T> mMainThreadCallback;
    final ThreadUtil.MainThreadCallback<T> mMainThreadProxy;
    final SparseIntArray mMissingPositions;
    final int[] mPrevRange = new int[2];
    int mRequestedGeneration;
    private int mScrollHint = 0;
    final Class<T> mTClass;
    final TileList<T> mTileList;
    final int mTileSize;
    final int[] mTmpRange = new int[2];
    final int[] mTmpRangeExtended = new int[2];
    final ViewCallback mViewCallback;

    public AsyncListUtil(Class<T> object, int n, DataCallback<T> dataCallback, ViewCallback viewCallback) {
        this.mRequestedGeneration = this.mDisplayedGeneration = 0;
        this.mMissingPositions = new SparseIntArray();
        this.mMainThreadCallback = new ThreadUtil.MainThreadCallback<T>(){

            private boolean isRequestedGeneration(int n) {
                if (n == AsyncListUtil.this.mRequestedGeneration) {
                    return true;
                }
                return false;
            }

            private void recycleAllTiles() {
                for (int i = 0; i < AsyncListUtil.this.mTileList.size(); ++i) {
                    AsyncListUtil.this.mBackgroundProxy.recycleTile(AsyncListUtil.this.mTileList.getAtIndex(i));
                }
                AsyncListUtil.this.mTileList.clear();
            }

            @Override
            public void addTile(int n, TileList.Tile<T> tile) {
                if (!this.isRequestedGeneration(n)) {
                    AsyncListUtil.this.mBackgroundProxy.recycleTile(tile);
                    return;
                }
                TileList.Tile tile2 = AsyncListUtil.this.mTileList.addOrReplace(tile);
                if (tile2 != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("duplicate tile @");
                    stringBuilder.append(tile2.mStartPosition);
                    Log.e((String)"AsyncListUtil", (String)stringBuilder.toString());
                    AsyncListUtil.this.mBackgroundProxy.recycleTile(tile2);
                }
                int n2 = tile.mStartPosition;
                int n3 = tile.mItemCount;
                n = 0;
                while (n < AsyncListUtil.this.mMissingPositions.size()) {
                    int n4 = AsyncListUtil.this.mMissingPositions.keyAt(n);
                    if (tile.mStartPosition <= n4 && n4 < n2 + n3) {
                        AsyncListUtil.this.mMissingPositions.removeAt(n);
                        AsyncListUtil.this.mViewCallback.onItemLoaded(n4);
                        continue;
                    }
                    ++n;
                }
            }

            @Override
            public void removeTile(int n, int n2) {
                if (!this.isRequestedGeneration(n)) {
                    return;
                }
                TileList.Tile tile = AsyncListUtil.this.mTileList.removeAtPos(n2);
                if (tile == null) {
                    tile = new StringBuilder();
                    tile.append("tile not found @");
                    tile.append(n2);
                    Log.e((String)"AsyncListUtil", (String)tile.toString());
                    return;
                }
                AsyncListUtil.this.mBackgroundProxy.recycleTile(tile);
            }

            @Override
            public void updateItemCount(int n, int n2) {
                if (!this.isRequestedGeneration(n)) {
                    return;
                }
                AsyncListUtil asyncListUtil = AsyncListUtil.this;
                asyncListUtil.mItemCount = n2;
                asyncListUtil.mViewCallback.onDataRefresh();
                asyncListUtil = AsyncListUtil.this;
                asyncListUtil.mDisplayedGeneration = asyncListUtil.mRequestedGeneration;
                this.recycleAllTiles();
                asyncListUtil = AsyncListUtil.this;
                asyncListUtil.mAllowScrollHints = false;
                asyncListUtil.updateRange();
            }
        };
        this.mBackgroundCallback = new ThreadUtil.BackgroundCallback<T>(){
            private int mFirstRequiredTileStart;
            private int mGeneration;
            private int mItemCount;
            private int mLastRequiredTileStart;
            final SparseBooleanArray mLoadedTiles;
            private TileList.Tile<T> mRecycledRoot;
            {
                this.mLoadedTiles = new SparseBooleanArray();
            }

            private TileList.Tile<T> acquireTile() {
                TileList.Tile<T> tile = this.mRecycledRoot;
                if (tile != null) {
                    TileList.Tile<T> tile2 = this.mRecycledRoot;
                    this.mRecycledRoot = tile.mNext;
                    return tile2;
                }
                return new TileList.Tile(AsyncListUtil.this.mTClass, AsyncListUtil.this.mTileSize);
            }

            private void addTile(TileList.Tile<T> tile) {
                this.mLoadedTiles.put(tile.mStartPosition, true);
                AsyncListUtil.this.mMainThreadProxy.addTile(this.mGeneration, tile);
            }

            private void flushTileCache(int n) {
                int n2 = AsyncListUtil.this.mDataCallback.getMaxCachedTiles();
                while (this.mLoadedTiles.size() >= n2) {
                    int n3 = this.mLoadedTiles.keyAt(0);
                    SparseBooleanArray sparseBooleanArray = this.mLoadedTiles;
                    int n4 = sparseBooleanArray.keyAt(sparseBooleanArray.size() - 1);
                    int n5 = this.mFirstRequiredTileStart - n3;
                    int n6 = n4 - this.mLastRequiredTileStart;
                    if (n5 > 0 && (n5 >= n6 || n == 2)) {
                        this.removeTile(n3);
                        continue;
                    }
                    if (n6 > 0 && (n5 < n6 || n == 1)) {
                        this.removeTile(n4);
                        continue;
                    }
                    return;
                }
            }

            private int getTileStart(int n) {
                return n - n % AsyncListUtil.this.mTileSize;
            }

            private boolean isTileLoaded(int n) {
                return this.mLoadedTiles.get(n);
            }

            private /* varargs */ void log(String string2, Object ... arrobject) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[BKGR] ");
                stringBuilder.append(String.format(string2, arrobject));
                Log.d((String)"AsyncListUtil", (String)stringBuilder.toString());
            }

            private void removeTile(int n) {
                this.mLoadedTiles.delete(n);
                AsyncListUtil.this.mMainThreadProxy.removeTile(this.mGeneration, n);
            }

            private void requestTiles(int n, int n2, int n3, boolean bl) {
                for (int i = n; i <= n2; i += AsyncListUtil.this.mTileSize) {
                    int n4 = bl ? n2 + n - i : i;
                    AsyncListUtil.this.mBackgroundProxy.loadTile(n4, n3);
                }
            }

            @Override
            public void loadTile(int n, int n2) {
                if (this.isTileLoaded(n)) {
                    return;
                }
                TileList.Tile<T> tile = this.acquireTile();
                tile.mStartPosition = n;
                tile.mItemCount = Math.min(AsyncListUtil.this.mTileSize, this.mItemCount - tile.mStartPosition);
                AsyncListUtil.this.mDataCallback.fillData(tile.mItems, tile.mStartPosition, tile.mItemCount);
                this.flushTileCache(n2);
                this.addTile(tile);
            }

            @Override
            public void recycleTile(TileList.Tile<T> tile) {
                AsyncListUtil.this.mDataCallback.recycleData(tile.mItems, tile.mItemCount);
                tile.mNext = this.mRecycledRoot;
                this.mRecycledRoot = tile;
            }

            @Override
            public void refresh(int n) {
                this.mGeneration = n;
                this.mLoadedTiles.clear();
                this.mItemCount = AsyncListUtil.this.mDataCallback.refreshData();
                AsyncListUtil.this.mMainThreadProxy.updateItemCount(this.mGeneration, this.mItemCount);
            }

            @Override
            public void updateRange(int n, int n2, int n3, int n4, int n5) {
                if (n > n2) {
                    return;
                }
                n = this.getTileStart(n);
                n2 = this.getTileStart(n2);
                this.mFirstRequiredTileStart = this.getTileStart(n3);
                this.mLastRequiredTileStart = this.getTileStart(n4);
                if (n5 == 1) {
                    this.requestTiles(this.mFirstRequiredTileStart, n2, n5, true);
                    this.requestTiles(AsyncListUtil.this.mTileSize + n2, this.mLastRequiredTileStart, n5, false);
                    return;
                }
                this.requestTiles(n, this.mLastRequiredTileStart, n5, false);
                this.requestTiles(this.mFirstRequiredTileStart, n - AsyncListUtil.this.mTileSize, n5, true);
            }
        };
        this.mTClass = object;
        this.mTileSize = n;
        this.mDataCallback = dataCallback;
        this.mViewCallback = viewCallback;
        this.mTileList = new TileList(this.mTileSize);
        object = new MessageThreadUtil();
        this.mMainThreadProxy = object.getMainThreadProxy(this.mMainThreadCallback);
        this.mBackgroundProxy = object.getBackgroundProxy(this.mBackgroundCallback);
        this.refresh();
    }

    private boolean isRefreshPending() {
        if (this.mRequestedGeneration != this.mDisplayedGeneration) {
            return true;
        }
        return false;
    }

    public T getItem(int n) {
        if (n >= 0 && n < this.mItemCount) {
            T t = this.mTileList.getItemAt(n);
            if (t == null && !this.isRefreshPending()) {
                this.mMissingPositions.put(n, 0);
                return t;
            }
            return t;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(n);
        stringBuilder.append(" is not within 0 and ");
        stringBuilder.append(this.mItemCount);
        throw new IndexOutOfBoundsException(stringBuilder.toString());
    }

    public int getItemCount() {
        return this.mItemCount;
    }

    /* varargs */ void log(String string2, Object ... arrobject) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[MAIN] ");
        stringBuilder.append(String.format(string2, arrobject));
        Log.d((String)"AsyncListUtil", (String)stringBuilder.toString());
    }

    public void onRangeChanged() {
        if (this.isRefreshPending()) {
            return;
        }
        this.updateRange();
        this.mAllowScrollHints = true;
    }

    public void refresh() {
        int n;
        this.mMissingPositions.clear();
        ThreadUtil.BackgroundCallback<T> backgroundCallback = this.mBackgroundProxy;
        this.mRequestedGeneration = n = this.mRequestedGeneration + 1;
        backgroundCallback.refresh(n);
    }

    void updateRange() {
        this.mViewCallback.getItemRangeInto(this.mTmpRange);
        Object object = this.mTmpRange;
        if (object[0] <= object[1]) {
            int[] arrn;
            int n;
            if (object[0] < 0) {
                return;
            }
            if (object[1] >= this.mItemCount) {
                return;
            }
            if (!this.mAllowScrollHints) {
                this.mScrollHint = 0;
            } else {
                n = object[0];
                arrn = this.mPrevRange;
                if (n <= arrn[1] && arrn[0] <= object[1]) {
                    if (object[0] < arrn[0]) {
                        this.mScrollHint = 1;
                    } else if (object[0] > arrn[0]) {
                        this.mScrollHint = 2;
                    }
                } else {
                    this.mScrollHint = 0;
                }
            }
            object = this.mPrevRange;
            arrn = this.mTmpRange;
            object[0] = arrn[0];
            object[1] = arrn[1];
            this.mViewCallback.extendRangeInto(arrn, this.mTmpRangeExtended, this.mScrollHint);
            object = this.mTmpRangeExtended;
            object[0] = Math.min(this.mTmpRange[0], Math.max(object[0], 0));
            object = this.mTmpRangeExtended;
            object[1] = Math.max(this.mTmpRange[1], Math.min(object[1], this.mItemCount - 1));
            object = this.mBackgroundProxy;
            arrn = this.mTmpRange;
            n = arrn[0];
            int n2 = arrn[1];
            arrn = this.mTmpRangeExtended;
            object.updateRange(n, n2, arrn[0], arrn[1], this.mScrollHint);
            return;
        }
    }

    public static abstract class DataCallback<T> {
        @WorkerThread
        public abstract void fillData(T[] var1, int var2, int var3);

        @WorkerThread
        public int getMaxCachedTiles() {
            return 10;
        }

        @WorkerThread
        public void recycleData(T[] arrT, int n) {
        }

        @WorkerThread
        public abstract int refreshData();
    }

    public static abstract class ViewCallback {
        public static final int HINT_SCROLL_ASC = 2;
        public static final int HINT_SCROLL_DESC = 1;
        public static final int HINT_SCROLL_NONE = 0;

        @UiThread
        public void extendRangeInto(int[] arrn, int[] arrn2, int n) {
            int n2 = arrn[1] - arrn[0] + 1;
            int n3 = n2 / 2;
            int n4 = arrn[0];
            int n5 = n == 1 ? n2 : n3;
            arrn2[0] = n4 - n5;
            n5 = arrn[1];
            if (n != 2) {
                n2 = n3;
            }
            arrn2[1] = n5 + n2;
        }

        @UiThread
        public abstract void getItemRangeInto(int[] var1);

        @UiThread
        public abstract void onDataRefresh();

        @UiThread
        public abstract void onItemLoaded(int var1);
    }

}

