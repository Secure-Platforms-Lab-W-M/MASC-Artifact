package androidx.viewpager2.adapter;

import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public final class FragmentViewHolder extends RecyclerView.ViewHolder {
   private FragmentViewHolder(FrameLayout var1) {
      super(var1);
   }

   static FragmentViewHolder create(ViewGroup var0) {
      FrameLayout var1 = new FrameLayout(var0.getContext());
      var1.setLayoutParams(new LayoutParams(-1, -1));
      var1.setId(ViewCompat.generateViewId());
      var1.setSaveEnabled(false);
      return new FragmentViewHolder(var1);
   }

   FrameLayout getContainer() {
      return (FrameLayout)this.itemView;
   }
}
