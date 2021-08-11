package com.bumptech.glide.util;

public class MultiClassKey {
   private Class first;
   private Class second;
   private Class third;

   public MultiClassKey() {
   }

   public MultiClassKey(Class var1, Class var2) {
      this.set(var1, var2);
   }

   public MultiClassKey(Class var1, Class var2, Class var3) {
      this.set(var1, var2, var3);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null) {
         if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            MultiClassKey var2 = (MultiClassKey)var1;
            if (!this.first.equals(var2.first)) {
               return false;
            } else if (!this.second.equals(var2.second)) {
               return false;
            } else {
               return Util.bothNullOrEqual(this.third, var2.third);
            }
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var2 = this.first.hashCode();
      int var3 = this.second.hashCode();
      Class var4 = this.third;
      int var1;
      if (var4 != null) {
         var1 = var4.hashCode();
      } else {
         var1 = 0;
      }

      return (var2 * 31 + var3) * 31 + var1;
   }

   public void set(Class var1, Class var2) {
      this.set(var1, var2, (Class)null);
   }

   public void set(Class var1, Class var2, Class var3) {
      this.first = var1;
      this.second = var2;
      this.third = var3;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("MultiClassKey{first=");
      var1.append(this.first);
      var1.append(", second=");
      var1.append(this.second);
      var1.append('}');
      return var1.toString();
   }
}
