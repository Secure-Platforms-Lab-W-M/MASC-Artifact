package org.apache.commons.text.similarity;

import java.util.Objects;

public class LevenshteinResults {
   private final Integer deleteCount;
   private final Integer distance;
   private final Integer insertCount;
   private final Integer substituteCount;

   public LevenshteinResults(Integer var1, Integer var2, Integer var3, Integer var4) {
      this.distance = var1;
      this.insertCount = var2;
      this.deleteCount = var3;
      this.substituteCount = var4;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null) {
         if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            LevenshteinResults var2 = (LevenshteinResults)var1;
            return Objects.equals(this.distance, var2.distance) && Objects.equals(this.insertCount, var2.insertCount) && Objects.equals(this.deleteCount, var2.deleteCount) && Objects.equals(this.substituteCount, var2.substituteCount);
         }
      } else {
         return false;
      }
   }

   public Integer getDeleteCount() {
      return this.deleteCount;
   }

   public Integer getDistance() {
      return this.distance;
   }

   public Integer getInsertCount() {
      return this.insertCount;
   }

   public Integer getSubstituteCount() {
      return this.substituteCount;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.distance, this.insertCount, this.deleteCount, this.substituteCount});
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Distance: ");
      var1.append(this.distance);
      var1.append(", Insert: ");
      var1.append(this.insertCount);
      var1.append(", Delete: ");
      var1.append(this.deleteCount);
      var1.append(", Substitute: ");
      var1.append(this.substituteCount);
      return var1.toString();
   }
}
