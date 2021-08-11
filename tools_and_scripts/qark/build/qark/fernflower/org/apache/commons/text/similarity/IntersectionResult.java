package org.apache.commons.text.similarity;

import java.util.Objects;

public class IntersectionResult {
   private final int intersection;
   private final int sizeA;
   private final int sizeB;

   public IntersectionResult(int var1, int var2, int var3) {
      StringBuilder var4;
      if (var1 >= 0) {
         if (var2 >= 0) {
            if (var3 >= 0 && var3 <= Math.min(var1, var2)) {
               this.sizeA = var1;
               this.sizeB = var2;
               this.intersection = var3;
            } else {
               var4 = new StringBuilder();
               var4.append("Invalid intersection of |A| and |B|: ");
               var4.append(var3);
               throw new IllegalArgumentException(var4.toString());
            }
         } else {
            var4 = new StringBuilder();
            var4.append("Set size |B| is not positive: ");
            var4.append(var2);
            throw new IllegalArgumentException(var4.toString());
         }
      } else {
         var4 = new StringBuilder();
         var4.append("Set size |A| is not positive: ");
         var4.append(var1);
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null) {
         if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            IntersectionResult var2 = (IntersectionResult)var1;
            return this.sizeA == var2.sizeA && this.sizeB == var2.sizeB && this.intersection == var2.intersection;
         }
      } else {
         return false;
      }
   }

   public int getIntersection() {
      return this.intersection;
   }

   public int getSizeA() {
      return this.sizeA;
   }

   public int getSizeB() {
      return this.sizeB;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.sizeA, this.sizeB, this.intersection});
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Size A: ");
      var1.append(this.sizeA);
      var1.append(", Size B: ");
      var1.append(this.sizeB);
      var1.append(", Intersection: ");
      var1.append(this.intersection);
      return var1.toString();
   }
}
