package androidx.lifecycle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ViewModelStore {
   private final HashMap mMap = new HashMap();

   public final void clear() {
      Iterator var1 = this.mMap.values().iterator();

      while(var1.hasNext()) {
         ((ViewModel)var1.next()).clear();
      }

      this.mMap.clear();
   }

   final ViewModel get(String var1) {
      return (ViewModel)this.mMap.get(var1);
   }

   Set keys() {
      return new HashSet(this.mMap.keySet());
   }

   final void put(String var1, ViewModel var2) {
      ViewModel var3 = (ViewModel)this.mMap.put(var1, var2);
      if (var3 != null) {
         var3.onCleared();
      }

   }
}
