package org.apache.commons.text.similarity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CosineSimilarity {
   private double dot(Map var1, Map var2, Set var3) {
      long var4 = 0L;

      CharSequence var6;
      for(Iterator var7 = var3.iterator(); var7.hasNext(); var4 += (long)((Integer)var1.get(var6) * (Integer)var2.get(var6))) {
         var6 = (CharSequence)var7.next();
      }

      return (double)var4;
   }

   private Set getIntersection(Map var1, Map var2) {
      HashSet var3 = new HashSet(var1.keySet());
      var3.retainAll(var2.keySet());
      return var3;
   }

   public Double cosineSimilarity(Map var1, Map var2) {
      if (var1 != null && var2 != null) {
         double var7 = this.dot(var1, var2, this.getIntersection(var1, var2));
         double var3 = 0.0D;

         Iterator var9;
         for(var9 = var1.values().iterator(); var9.hasNext(); var3 += Math.pow((double)(Integer)var9.next(), 2.0D)) {
         }

         double var5 = 0.0D;

         for(var9 = var2.values().iterator(); var9.hasNext(); var5 += Math.pow((double)(Integer)var9.next(), 2.0D)) {
         }

         if (var3 > 0.0D && var5 > 0.0D) {
            var3 = var7 / (Math.sqrt(var3) * Math.sqrt(var5));
         } else {
            var3 = 0.0D;
         }

         return var3;
      } else {
         throw new IllegalArgumentException("Vectors must not be null");
      }
   }
}
