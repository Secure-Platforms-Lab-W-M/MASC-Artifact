package androidx.recyclerview.widget;

import java.util.List;

public abstract class ListAdapter extends RecyclerView.Adapter {
   final AsyncListDiffer mDiffer;
   private final AsyncListDiffer.ListListener mListener = new AsyncListDiffer.ListListener() {
      public void onCurrentListChanged(List var1, List var2) {
         ListAdapter.this.onCurrentListChanged(var1, var2);
      }
   };

   protected ListAdapter(AsyncDifferConfig var1) {
      AsyncListDiffer var2 = new AsyncListDiffer(new AdapterListUpdateCallback(this), var1);
      this.mDiffer = var2;
      var2.addListListener(this.mListener);
   }

   protected ListAdapter(DiffUtil.ItemCallback var1) {
      AsyncListDiffer var2 = new AsyncListDiffer(new AdapterListUpdateCallback(this), (new AsyncDifferConfig.Builder(var1)).build());
      this.mDiffer = var2;
      var2.addListListener(this.mListener);
   }

   public List getCurrentList() {
      return this.mDiffer.getCurrentList();
   }

   protected Object getItem(int var1) {
      return this.mDiffer.getCurrentList().get(var1);
   }

   public int getItemCount() {
      return this.mDiffer.getCurrentList().size();
   }

   public void onCurrentListChanged(List var1, List var2) {
   }

   public void submitList(List var1) {
      this.mDiffer.submitList(var1);
   }

   public void submitList(List var1, Runnable var2) {
      this.mDiffer.submitList(var1, var2);
   }
}
