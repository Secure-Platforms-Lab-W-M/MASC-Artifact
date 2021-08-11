package butterknife.internal;

import java.util.AbstractList;
import java.util.RandomAccess;

final class ImmutableList extends AbstractList implements RandomAccess {
   private final Object[] views;

   ImmutableList(Object[] var1) {
      this.views = var1;
   }

   public boolean contains(Object var1) {
      Object[] var4 = this.views;
      int var3 = var4.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (var4[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   public Object get(int var1) {
      return this.views[var1];
   }

   public int size() {
      return this.views.length;
   }
}
