package androidx.arch.core.internal;

import java.util.HashMap;

public class FastSafeIterableMap extends SafeIterableMap {
   private HashMap mHashMap = new HashMap();

   public java.util.Map.Entry ceil(Object var1) {
      return this.contains(var1) ? ((SafeIterableMap.Entry)this.mHashMap.get(var1)).mPrevious : null;
   }

   public boolean contains(Object var1) {
      return this.mHashMap.containsKey(var1);
   }

   protected SafeIterableMap.Entry get(Object var1) {
      return (SafeIterableMap.Entry)this.mHashMap.get(var1);
   }

   public Object putIfAbsent(Object var1, Object var2) {
      SafeIterableMap.Entry var3 = this.get(var1);
      if (var3 != null) {
         return var3.mValue;
      } else {
         this.mHashMap.put(var1, this.put(var1, var2));
         return null;
      }
   }

   public Object remove(Object var1) {
      Object var2 = super.remove(var1);
      this.mHashMap.remove(var1);
      return var2;
   }
}
