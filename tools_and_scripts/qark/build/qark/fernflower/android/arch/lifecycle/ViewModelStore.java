package android.arch.lifecycle;

import java.util.HashMap;
import java.util.Iterator;

public class ViewModelStore {
   private final HashMap mMap = new HashMap();

   public final void clear() {
      Iterator var1 = this.mMap.values().iterator();

      while(var1.hasNext()) {
         ((ViewModel)var1.next()).onCleared();
      }

      this.mMap.clear();
   }

   final ViewModel get(String var1) {
      return (ViewModel)this.mMap.get(var1);
   }

   final void put(String var1, ViewModel var2) {
      ViewModel var3 = (ViewModel)this.mMap.get(var1);
      if (var3 != null) {
         var3.onCleared();
      }

      this.mMap.put(var1, var2);
   }
}
