package androidx.databinding;

import java.util.List;

public interface ObservableList extends List {
   void addOnListChangedCallback(ObservableList.OnListChangedCallback var1);

   void removeOnListChangedCallback(ObservableList.OnListChangedCallback var1);

   public abstract static class OnListChangedCallback {
      public abstract void onChanged(ObservableList var1);

      public abstract void onItemRangeChanged(ObservableList var1, int var2, int var3);

      public abstract void onItemRangeInserted(ObservableList var1, int var2, int var3);

      public abstract void onItemRangeMoved(ObservableList var1, int var2, int var3, int var4);

      public abstract void onItemRangeRemoved(ObservableList var1, int var2, int var3);
   }
}
