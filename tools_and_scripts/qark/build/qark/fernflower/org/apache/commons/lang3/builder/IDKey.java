package org.apache.commons.lang3.builder;

final class IDKey {
   // $FF: renamed from: id int
   private final int field_207;
   private final Object value;

   IDKey(Object var1) {
      this.field_207 = System.identityHashCode(var1);
      this.value = var1;
   }

   public boolean equals(Object var1) {
      boolean var3 = var1 instanceof IDKey;
      boolean var2 = false;
      if (!var3) {
         return false;
      } else {
         IDKey var4 = (IDKey)var1;
         if (this.field_207 != var4.field_207) {
            return false;
         } else {
            if (this.value == var4.value) {
               var2 = true;
            }

            return var2;
         }
      }
   }

   public int hashCode() {
      return this.field_207;
   }
}
