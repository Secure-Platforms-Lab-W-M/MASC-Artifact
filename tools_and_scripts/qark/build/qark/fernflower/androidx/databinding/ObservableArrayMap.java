package androidx.databinding;

import androidx.collection.ArrayMap;
import java.util.Collection;
import java.util.Iterator;

public class ObservableArrayMap extends ArrayMap implements ObservableMap {
   private transient MapChangeRegistry mListeners;

   private void notifyChange(Object var1) {
      MapChangeRegistry var2 = this.mListeners;
      if (var2 != null) {
         var2.notifyCallbacks(this, 0, var1);
      }

   }

   public void addOnMapChangedCallback(ObservableMap.OnMapChangedCallback var1) {
      if (this.mListeners == null) {
         this.mListeners = new MapChangeRegistry();
      }

      this.mListeners.add(var1);
   }

   public void clear() {
      if (!this.isEmpty()) {
         super.clear();
         this.notifyChange((Object)null);
      }

   }

   public Object put(Object var1, Object var2) {
      super.put(var1, var2);
      this.notifyChange(var1);
      return var2;
   }

   public boolean removeAll(Collection var1) {
      boolean var3 = false;
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         int var2 = this.indexOfKey(var4.next());
         if (var2 >= 0) {
            var3 = true;
            this.removeAt(var2);
         }
      }

      return var3;
   }

   public Object removeAt(int var1) {
      Object var2 = this.keyAt(var1);
      Object var3 = super.removeAt(var1);
      if (var3 != null) {
         this.notifyChange(var2);
      }

      return var3;
   }

   public void removeOnMapChangedCallback(ObservableMap.OnMapChangedCallback var1) {
      MapChangeRegistry var2 = this.mListeners;
      if (var2 != null) {
         var2.remove(var1);
      }

   }

   public boolean retainAll(Collection var1) {
      boolean var3 = false;

      for(int var2 = this.size() - 1; var2 >= 0; --var2) {
         if (!var1.contains(this.keyAt(var2))) {
            this.removeAt(var2);
            var3 = true;
         }
      }

      return var3;
   }

   public Object setValueAt(int var1, Object var2) {
      Object var3 = this.keyAt(var1);
      var2 = super.setValueAt(var1, var2);
      this.notifyChange(var3);
      return var2;
   }
}
