/*
 * Decompiled with CFR 0_124.
 */
package android.support.v7.util;

import android.support.v7.util.TileList;

interface ThreadUtil<T> {
    public BackgroundCallback<T> getBackgroundProxy(BackgroundCallback<T> var1);

    public MainThreadCallback<T> getMainThreadProxy(MainThreadCallback<T> var1);

    public static interface BackgroundCallback<T> {
        public void loadTile(int var1, int var2);

        public void recycleTile(TileList.Tile<T> var1);

        public void refresh(int var1);

        public void updateRange(int var1, int var2, int var3, int var4, int var5);
    }

    public static interface MainThreadCallback<T> {
        public void addTile(int var1, TileList.Tile<T> var2);

        public void removeTile(int var1, int var2);

        public void updateItemCount(int var1, int var2);
    }

}

