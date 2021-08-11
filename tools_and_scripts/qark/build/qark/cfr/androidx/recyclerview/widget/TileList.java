/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package androidx.recyclerview.widget;

import android.util.SparseArray;
import java.lang.reflect.Array;

class TileList<T> {
    Tile<T> mLastAccessedTile;
    final int mTileSize;
    private final SparseArray<Tile<T>> mTiles = new SparseArray(10);

    public TileList(int n) {
        this.mTileSize = n;
    }

    public Tile<T> addOrReplace(Tile<T> tile) {
        int n = this.mTiles.indexOfKey(tile.mStartPosition);
        if (n < 0) {
            this.mTiles.put(tile.mStartPosition, tile);
            return null;
        }
        Tile tile2 = (Tile)this.mTiles.valueAt(n);
        this.mTiles.setValueAt(n, tile);
        if (this.mLastAccessedTile == tile2) {
            this.mLastAccessedTile = tile;
        }
        return tile2;
    }

    public void clear() {
        this.mTiles.clear();
    }

    public Tile<T> getAtIndex(int n) {
        return (Tile)this.mTiles.valueAt(n);
    }

    public T getItemAt(int n) {
        Tile<T> tile = this.mLastAccessedTile;
        if (tile == null || !tile.containsPosition(n)) {
            int n2 = this.mTileSize;
            if ((n2 = this.mTiles.indexOfKey(n - n % n2)) < 0) {
                return null;
            }
            this.mLastAccessedTile = (Tile)this.mTiles.valueAt(n2);
        }
        return this.mLastAccessedTile.getByPosition(n);
    }

    public Tile<T> removeAtPos(int n) {
        Tile tile = (Tile)this.mTiles.get(n);
        if (this.mLastAccessedTile == tile) {
            this.mLastAccessedTile = null;
        }
        this.mTiles.delete(n);
        return tile;
    }

    public int size() {
        return this.mTiles.size();
    }

    public static class Tile<T> {
        public int mItemCount;
        public final T[] mItems;
        Tile<T> mNext;
        public int mStartPosition;

        public Tile(Class<T> class_, int n) {
            this.mItems = (Object[])Array.newInstance(class_, n);
        }

        boolean containsPosition(int n) {
            int n2 = this.mStartPosition;
            if (n2 <= n && n < n2 + this.mItemCount) {
                return true;
            }
            return false;
        }

        T getByPosition(int n) {
            return this.mItems[n - this.mStartPosition];
        }
    }

}

