package androidx.recyclerview.widget;

interface ThreadUtil {
   ThreadUtil.BackgroundCallback getBackgroundProxy(ThreadUtil.BackgroundCallback var1);

   ThreadUtil.MainThreadCallback getMainThreadProxy(ThreadUtil.MainThreadCallback var1);

   public interface BackgroundCallback {
      void loadTile(int var1, int var2);

      void recycleTile(TileList.Tile var1);

      void refresh(int var1);

      void updateRange(int var1, int var2, int var3, int var4, int var5);
   }

   public interface MainThreadCallback {
      void addTile(int var1, TileList.Tile var2);

      void removeTile(int var1, int var2);

      void updateItemCount(int var1, int var2);
   }
}
