package androidx.databinding;

import java.util.ArrayList;
import java.util.Collection;

public class ObservableArrayList extends ArrayList implements ObservableList {
   private transient ListChangeRegistry mListeners = new ListChangeRegistry();

   private void notifyAdd(int var1, int var2) {
      ListChangeRegistry var3 = this.mListeners;
      if (var3 != null) {
         var3.notifyInserted(this, var1, var2);
      }

   }

   private void notifyRemove(int var1, int var2) {
      ListChangeRegistry var3 = this.mListeners;
      if (var3 != null) {
         var3.notifyRemoved(this, var1, var2);
      }

   }

   public void add(int var1, Object var2) {
      super.add(var1, var2);
      this.notifyAdd(var1, 1);
   }

   public boolean add(Object var1) {
      super.add(var1);
      this.notifyAdd(this.size() - 1, 1);
      return true;
   }

   public boolean addAll(int var1, Collection var2) {
      boolean var3 = super.addAll(var1, var2);
      if (var3) {
         this.notifyAdd(var1, var2.size());
      }

      return var3;
   }

   public boolean addAll(Collection var1) {
      int var2 = this.size();
      boolean var3 = super.addAll(var1);
      if (var3) {
         this.notifyAdd(var2, this.size() - var2);
      }

      return var3;
   }

   public void addOnListChangedCallback(ObservableList.OnListChangedCallback var1) {
      if (this.mListeners == null) {
         this.mListeners = new ListChangeRegistry();
      }

      this.mListeners.add(var1);
   }

   public void clear() {
      int var1 = this.size();
      super.clear();
      if (var1 != 0) {
         this.notifyRemove(0, var1);
      }

   }

   public Object remove(int var1) {
      Object var2 = super.remove(var1);
      this.notifyRemove(var1, 1);
      return var2;
   }

   public boolean remove(Object var1) {
      int var2 = this.indexOf(var1);
      if (var2 >= 0) {
         this.remove(var2);
         return true;
      } else {
         return false;
      }
   }

   public void removeOnListChangedCallback(ObservableList.OnListChangedCallback var1) {
      ListChangeRegistry var2 = this.mListeners;
      if (var2 != null) {
         var2.remove(var1);
      }

   }

   protected void removeRange(int var1, int var2) {
      super.removeRange(var1, var2);
      this.notifyRemove(var1, var2 - var1);
   }

   public Object set(int var1, Object var2) {
      var2 = super.set(var1, var2);
      ListChangeRegistry var3 = this.mListeners;
      if (var3 != null) {
         var3.notifyChanged(this, var1, 1);
      }

      return var2;
   }
}
