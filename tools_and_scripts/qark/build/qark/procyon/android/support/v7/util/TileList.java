// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.util;

import java.lang.reflect.Array;
import android.util.SparseArray;

class TileList<T>
{
    Tile<T> mLastAccessedTile;
    final int mTileSize;
    private final SparseArray<Tile<T>> mTiles;
    
    public TileList(final int mTileSize) {
        this.mTiles = (SparseArray<Tile<T>>)new SparseArray(10);
        this.mTileSize = mTileSize;
    }
    
    public Tile<T> addOrReplace(final Tile<T> mLastAccessedTile) {
        final int indexOfKey = this.mTiles.indexOfKey(mLastAccessedTile.mStartPosition);
        if (indexOfKey < 0) {
            this.mTiles.put(mLastAccessedTile.mStartPosition, (Object)mLastAccessedTile);
            return null;
        }
        final Tile tile = (Tile)this.mTiles.valueAt(indexOfKey);
        this.mTiles.setValueAt(indexOfKey, (Object)mLastAccessedTile);
        if (this.mLastAccessedTile == tile) {
            this.mLastAccessedTile = mLastAccessedTile;
            return (Tile<T>)tile;
        }
        return (Tile<T>)tile;
    }
    
    public void clear() {
        this.mTiles.clear();
    }
    
    public Tile<T> getAtIndex(final int n) {
        return (Tile<T>)this.mTiles.valueAt(n);
    }
    
    public T getItemAt(final int n) {
        final Tile<T> mLastAccessedTile = this.mLastAccessedTile;
        if (mLastAccessedTile == null || !mLastAccessedTile.containsPosition(n)) {
            final int indexOfKey = this.mTiles.indexOfKey(n - n % this.mTileSize);
            if (indexOfKey < 0) {
                return null;
            }
            this.mLastAccessedTile = (Tile<T>)this.mTiles.valueAt(indexOfKey);
        }
        return this.mLastAccessedTile.getByPosition(n);
    }
    
    public Tile<T> removeAtPos(final int n) {
        final Tile tile = (Tile)this.mTiles.get(n);
        if (this.mLastAccessedTile == tile) {
            this.mLastAccessedTile = null;
        }
        this.mTiles.delete(n);
        return (Tile<T>)tile;
    }
    
    public int size() {
        return this.mTiles.size();
    }
    
    public static class Tile<T>
    {
        public int mItemCount;
        public final T[] mItems;
        Tile<T> mNext;
        public int mStartPosition;
        
        public Tile(final Class<T> clazz, final int n) {
            this.mItems = (T[])Array.newInstance(clazz, n);
        }
        
        boolean containsPosition(final int n) {
            final int mStartPosition = this.mStartPosition;
            return mStartPosition <= n && n < mStartPosition + this.mItemCount;
        }
        
        T getByPosition(final int n) {
            return this.mItems[n - this.mStartPosition];
        }
    }
}
