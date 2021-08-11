// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.util;

import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.SparseBooleanArray;
import android.util.Log;
import android.util.SparseIntArray;

public class AsyncListUtil<T>
{
    static final boolean DEBUG = false;
    static final String TAG = "AsyncListUtil";
    boolean mAllowScrollHints;
    private final ThreadUtil.BackgroundCallback<T> mBackgroundCallback;
    final ThreadUtil.BackgroundCallback<T> mBackgroundProxy;
    final DataCallback<T> mDataCallback;
    int mDisplayedGeneration;
    int mItemCount;
    private final ThreadUtil.MainThreadCallback<T> mMainThreadCallback;
    final ThreadUtil.MainThreadCallback<T> mMainThreadProxy;
    final SparseIntArray mMissingPositions;
    final int[] mPrevRange;
    int mRequestedGeneration;
    private int mScrollHint;
    final Class<T> mTClass;
    final TileList<T> mTileList;
    final int mTileSize;
    final int[] mTmpRange;
    final int[] mTmpRangeExtended;
    final ViewCallback mViewCallback;
    
    public AsyncListUtil(final Class<T> mtClass, final int mTileSize, final DataCallback<T> mDataCallback, final ViewCallback mViewCallback) {
        this.mTmpRange = new int[2];
        this.mPrevRange = new int[2];
        this.mTmpRangeExtended = new int[2];
        this.mScrollHint = 0;
        this.mItemCount = 0;
        this.mDisplayedGeneration = 0;
        this.mRequestedGeneration = this.mDisplayedGeneration;
        this.mMissingPositions = new SparseIntArray();
        this.mMainThreadCallback = new ThreadUtil.MainThreadCallback<T>() {
            private boolean isRequestedGeneration(final int n) {
                return n == AsyncListUtil.this.mRequestedGeneration;
            }
            
            private void recycleAllTiles() {
                for (int i = 0; i < AsyncListUtil.this.mTileList.size(); ++i) {
                    AsyncListUtil.this.mBackgroundProxy.recycleTile(AsyncListUtil.this.mTileList.getAtIndex(i));
                }
                AsyncListUtil.this.mTileList.clear();
            }
            
            @Override
            public void addTile(int i, final TileList.Tile<T> tile) {
                if (!this.isRequestedGeneration(i)) {
                    AsyncListUtil.this.mBackgroundProxy.recycleTile(tile);
                    return;
                }
                final TileList.Tile<T> addOrReplace = AsyncListUtil.this.mTileList.addOrReplace(tile);
                if (addOrReplace != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("duplicate tile @");
                    sb.append(addOrReplace.mStartPosition);
                    Log.e("AsyncListUtil", sb.toString());
                    AsyncListUtil.this.mBackgroundProxy.recycleTile(addOrReplace);
                }
                final int mStartPosition = tile.mStartPosition;
                final int mItemCount = tile.mItemCount;
                i = 0;
                while (i < AsyncListUtil.this.mMissingPositions.size()) {
                    final int key = AsyncListUtil.this.mMissingPositions.keyAt(i);
                    if (tile.mStartPosition <= key && key < mStartPosition + mItemCount) {
                        AsyncListUtil.this.mMissingPositions.removeAt(i);
                        AsyncListUtil.this.mViewCallback.onItemLoaded(key);
                    }
                    else {
                        ++i;
                    }
                }
            }
            
            @Override
            public void removeTile(final int n, final int n2) {
                if (!this.isRequestedGeneration(n)) {
                    return;
                }
                final TileList.Tile<T> removeAtPos = AsyncListUtil.this.mTileList.removeAtPos(n2);
                if (removeAtPos == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("tile not found @");
                    sb.append(n2);
                    Log.e("AsyncListUtil", sb.toString());
                    return;
                }
                AsyncListUtil.this.mBackgroundProxy.recycleTile(removeAtPos);
            }
            
            @Override
            public void updateItemCount(final int n, final int mItemCount) {
                if (!this.isRequestedGeneration(n)) {
                    return;
                }
                final AsyncListUtil this$0 = AsyncListUtil.this;
                this$0.mItemCount = mItemCount;
                this$0.mViewCallback.onDataRefresh();
                final AsyncListUtil this$2 = AsyncListUtil.this;
                this$2.mDisplayedGeneration = this$2.mRequestedGeneration;
                this.recycleAllTiles();
                final AsyncListUtil this$3 = AsyncListUtil.this;
                this$3.mAllowScrollHints = false;
                this$3.updateRange();
            }
        };
        this.mBackgroundCallback = new ThreadUtil.BackgroundCallback<T>() {
            private int mFirstRequiredTileStart;
            private int mGeneration;
            private int mItemCount;
            private int mLastRequiredTileStart;
            final SparseBooleanArray mLoadedTiles = new SparseBooleanArray();
            private TileList.Tile<T> mRecycledRoot;
            
            private TileList.Tile<T> acquireTile() {
                final TileList.Tile<T> mRecycledRoot = this.mRecycledRoot;
                if (mRecycledRoot != null) {
                    final TileList.Tile<T> mRecycledRoot2 = this.mRecycledRoot;
                    this.mRecycledRoot = mRecycledRoot.mNext;
                    return mRecycledRoot2;
                }
                return (TileList.Tile<T>)new TileList.Tile((Class<Object>)AsyncListUtil.this.mTClass, AsyncListUtil.this.mTileSize);
            }
            
            private void addTile(final TileList.Tile<T> tile) {
                this.mLoadedTiles.put(tile.mStartPosition, true);
                AsyncListUtil.this.mMainThreadProxy.addTile(this.mGeneration, tile);
            }
            
            private void flushTileCache(final int n) {
                while (this.mLoadedTiles.size() >= AsyncListUtil.this.mDataCallback.getMaxCachedTiles()) {
                    final int key = this.mLoadedTiles.keyAt(0);
                    final SparseBooleanArray mLoadedTiles = this.mLoadedTiles;
                    final int key2 = mLoadedTiles.keyAt(mLoadedTiles.size() - 1);
                    final int n2 = this.mFirstRequiredTileStart - key;
                    final int n3 = key2 - this.mLastRequiredTileStart;
                    if (n2 > 0 && (n2 >= n3 || n == 2)) {
                        this.removeTile(key);
                    }
                    else {
                        if (n3 <= 0 || (n2 >= n3 && n != 1)) {
                            return;
                        }
                        this.removeTile(key2);
                    }
                }
            }
            
            private int getTileStart(final int n) {
                return n - n % AsyncListUtil.this.mTileSize;
            }
            
            private boolean isTileLoaded(final int n) {
                return this.mLoadedTiles.get(n);
            }
            
            private void log(final String s, final Object... array) {
                final StringBuilder sb = new StringBuilder();
                sb.append("[BKGR] ");
                sb.append(String.format(s, array));
                Log.d("AsyncListUtil", sb.toString());
            }
            
            private void removeTile(final int n) {
                this.mLoadedTiles.delete(n);
                AsyncListUtil.this.mMainThreadProxy.removeTile(this.mGeneration, n);
            }
            
            private void requestTiles(final int n, final int n2, final int n3, final boolean b) {
                for (int i = n; i <= n2; i += AsyncListUtil.this.mTileSize) {
                    int n4;
                    if (b) {
                        n4 = n2 + n - i;
                    }
                    else {
                        n4 = i;
                    }
                    AsyncListUtil.this.mBackgroundProxy.loadTile(n4, n3);
                }
            }
            
            @Override
            public void loadTile(final int mStartPosition, final int n) {
                if (this.isTileLoaded(mStartPosition)) {
                    return;
                }
                final TileList.Tile<T> acquireTile = this.acquireTile();
                acquireTile.mStartPosition = mStartPosition;
                acquireTile.mItemCount = Math.min(AsyncListUtil.this.mTileSize, this.mItemCount - acquireTile.mStartPosition);
                AsyncListUtil.this.mDataCallback.fillData(acquireTile.mItems, acquireTile.mStartPosition, acquireTile.mItemCount);
                this.flushTileCache(n);
                this.addTile(acquireTile);
            }
            
            @Override
            public void recycleTile(final TileList.Tile<T> mRecycledRoot) {
                AsyncListUtil.this.mDataCallback.recycleData(mRecycledRoot.mItems, mRecycledRoot.mItemCount);
                mRecycledRoot.mNext = this.mRecycledRoot;
                this.mRecycledRoot = mRecycledRoot;
            }
            
            @Override
            public void refresh(final int mGeneration) {
                this.mGeneration = mGeneration;
                this.mLoadedTiles.clear();
                this.mItemCount = AsyncListUtil.this.mDataCallback.refreshData();
                AsyncListUtil.this.mMainThreadProxy.updateItemCount(this.mGeneration, this.mItemCount);
            }
            
            @Override
            public void updateRange(int tileStart, int tileStart2, final int n, final int n2, final int n3) {
                if (tileStart > tileStart2) {
                    return;
                }
                tileStart = this.getTileStart(tileStart);
                tileStart2 = this.getTileStart(tileStart2);
                this.mFirstRequiredTileStart = this.getTileStart(n);
                this.mLastRequiredTileStart = this.getTileStart(n2);
                if (n3 == 1) {
                    this.requestTiles(this.mFirstRequiredTileStart, tileStart2, n3, true);
                    this.requestTiles(AsyncListUtil.this.mTileSize + tileStart2, this.mLastRequiredTileStart, n3, false);
                    return;
                }
                this.requestTiles(tileStart, this.mLastRequiredTileStart, n3, false);
                this.requestTiles(this.mFirstRequiredTileStart, tileStart - AsyncListUtil.this.mTileSize, n3, true);
            }
        };
        this.mTClass = mtClass;
        this.mTileSize = mTileSize;
        this.mDataCallback = mDataCallback;
        this.mViewCallback = mViewCallback;
        this.mTileList = new TileList<T>(this.mTileSize);
        final MessageThreadUtil<T> messageThreadUtil = new MessageThreadUtil<T>();
        this.mMainThreadProxy = messageThreadUtil.getMainThreadProxy(this.mMainThreadCallback);
        this.mBackgroundProxy = messageThreadUtil.getBackgroundProxy(this.mBackgroundCallback);
        this.refresh();
    }
    
    private boolean isRefreshPending() {
        return this.mRequestedGeneration != this.mDisplayedGeneration;
    }
    
    public T getItem(final int n) {
        if (n < 0 || n >= this.mItemCount) {
            final StringBuilder sb = new StringBuilder();
            sb.append(n);
            sb.append(" is not within 0 and ");
            sb.append(this.mItemCount);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        final T item = this.mTileList.getItemAt(n);
        if (item == null && !this.isRefreshPending()) {
            this.mMissingPositions.put(n, 0);
            return item;
        }
        return item;
    }
    
    public int getItemCount() {
        return this.mItemCount;
    }
    
    void log(final String s, final Object... array) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[MAIN] ");
        sb.append(String.format(s, array));
        Log.d("AsyncListUtil", sb.toString());
    }
    
    public void onRangeChanged() {
        if (this.isRefreshPending()) {
            return;
        }
        this.updateRange();
        this.mAllowScrollHints = true;
    }
    
    public void refresh() {
        this.mMissingPositions.clear();
        this.mBackgroundProxy.refresh(++this.mRequestedGeneration);
    }
    
    void updateRange() {
        this.mViewCallback.getItemRangeInto(this.mTmpRange);
        final int[] mTmpRange = this.mTmpRange;
        if (mTmpRange[0] > mTmpRange[1]) {
            return;
        }
        if (mTmpRange[0] < 0) {
            return;
        }
        if (mTmpRange[1] >= this.mItemCount) {
            return;
        }
        if (!this.mAllowScrollHints) {
            this.mScrollHint = 0;
        }
        else {
            final int n = mTmpRange[0];
            final int[] mPrevRange = this.mPrevRange;
            if (n <= mPrevRange[1] && mPrevRange[0] <= mTmpRange[1]) {
                if (mTmpRange[0] < mPrevRange[0]) {
                    this.mScrollHint = 1;
                }
                else if (mTmpRange[0] > mPrevRange[0]) {
                    this.mScrollHint = 2;
                }
            }
            else {
                this.mScrollHint = 0;
            }
        }
        final int[] mPrevRange2 = this.mPrevRange;
        final int[] mTmpRange2 = this.mTmpRange;
        mPrevRange2[0] = mTmpRange2[0];
        mPrevRange2[1] = mTmpRange2[1];
        this.mViewCallback.extendRangeInto(mTmpRange2, this.mTmpRangeExtended, this.mScrollHint);
        final int[] mTmpRangeExtended = this.mTmpRangeExtended;
        mTmpRangeExtended[0] = Math.min(this.mTmpRange[0], Math.max(mTmpRangeExtended[0], 0));
        final int[] mTmpRangeExtended2 = this.mTmpRangeExtended;
        mTmpRangeExtended2[1] = Math.max(this.mTmpRange[1], Math.min(mTmpRangeExtended2[1], this.mItemCount - 1));
        final ThreadUtil.BackgroundCallback<T> mBackgroundProxy = this.mBackgroundProxy;
        final int[] mTmpRange3 = this.mTmpRange;
        final int n2 = mTmpRange3[0];
        final int n3 = mTmpRange3[1];
        final int[] mTmpRangeExtended3 = this.mTmpRangeExtended;
        mBackgroundProxy.updateRange(n2, n3, mTmpRangeExtended3[0], mTmpRangeExtended3[1], this.mScrollHint);
    }
    
    public abstract static class DataCallback<T>
    {
        @WorkerThread
        public abstract void fillData(final T[] p0, final int p1, final int p2);
        
        @WorkerThread
        public int getMaxCachedTiles() {
            return 10;
        }
        
        @WorkerThread
        public void recycleData(final T[] array, final int n) {
        }
        
        @WorkerThread
        public abstract int refreshData();
    }
    
    public abstract static class ViewCallback
    {
        public static final int HINT_SCROLL_ASC = 2;
        public static final int HINT_SCROLL_DESC = 1;
        public static final int HINT_SCROLL_NONE = 0;
        
        @UiThread
        public void extendRangeInto(final int[] array, final int[] array2, final int n) {
            int n2 = array[1] - array[0] + 1;
            final int n3 = n2 / 2;
            final int n4 = array[0];
            int n5;
            if (n == 1) {
                n5 = n2;
            }
            else {
                n5 = n3;
            }
            array2[0] = n4 - n5;
            final int n6 = array[1];
            if (n != 2) {
                n2 = n3;
            }
            array2[1] = n6 + n2;
        }
        
        @UiThread
        public abstract void getItemRangeInto(final int[] p0);
        
        @UiThread
        public abstract void onDataRefresh();
        
        @UiThread
        public abstract void onItemLoaded(final int p0);
    }
}
