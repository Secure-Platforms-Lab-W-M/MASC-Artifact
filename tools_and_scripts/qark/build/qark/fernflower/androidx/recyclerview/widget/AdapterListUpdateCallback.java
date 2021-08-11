package androidx.recyclerview.widget;

public final class AdapterListUpdateCallback implements ListUpdateCallback {
   private final RecyclerView.Adapter mAdapter;

   public AdapterListUpdateCallback(RecyclerView.Adapter var1) {
      this.mAdapter = var1;
   }

   public void onChanged(int var1, int var2, Object var3) {
      this.mAdapter.notifyItemRangeChanged(var1, var2, var3);
   }

   public void onInserted(int var1, int var2) {
      this.mAdapter.notifyItemRangeInserted(var1, var2);
   }

   public void onMoved(int var1, int var2) {
      this.mAdapter.notifyItemMoved(var1, var2);
   }

   public void onRemoved(int var1, int var2) {
      this.mAdapter.notifyItemRangeRemoved(var1, var2);
   }
}
