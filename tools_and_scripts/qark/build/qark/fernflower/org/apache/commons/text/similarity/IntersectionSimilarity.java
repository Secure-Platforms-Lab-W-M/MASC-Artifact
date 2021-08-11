package org.apache.commons.text.similarity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;

public class IntersectionSimilarity implements SimilarityScore {
   private final Function converter;

   public IntersectionSimilarity(Function var1) {
      if (var1 != null) {
         this.converter = var1;
      } else {
         throw new IllegalArgumentException("Converter must not be null");
      }
   }

   private static int getIntersection(Set var0, Set var1) {
      int var2 = 0;

      int var3;
      for(Iterator var4 = var0.iterator(); var4.hasNext(); var2 = var3) {
         var3 = var2;
         if (var1.contains(var4.next())) {
            var3 = var2 + 1;
         }
      }

      return var2;
   }

   private int getIntersection(IntersectionSimilarity.TinyBag var1, IntersectionSimilarity.TinyBag var2) {
      int var3 = 0;

      Entry var4;
      Object var5;
      for(Iterator var6 = var1.entrySet().iterator(); var6.hasNext(); var3 += Math.min(((IntersectionSimilarity.BagCount)var4.getValue()).count, var2.getCount(var5))) {
         var4 = (Entry)var6.next();
         var5 = var4.getKey();
      }

      return var3;
   }

   private IntersectionSimilarity.TinyBag toBag(Collection var1) {
      IntersectionSimilarity.TinyBag var2 = new IntersectionSimilarity.TinyBag(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         var2.add(var3.next());
      }

      return var2;
   }

   public IntersectionResult apply(CharSequence var1, CharSequence var2) {
      if (var1 != null && var2 != null) {
         Collection var6 = (Collection)this.converter.apply(var1);
         Collection var8 = (Collection)this.converter.apply(var2);
         int var4 = var6.size();
         int var5 = var8.size();
         if (Math.min(var4, var5) == 0) {
            return new IntersectionResult(var4, var5, 0);
         } else {
            int var3;
            if (var6 instanceof Set && var8 instanceof Set) {
               if (var4 < var5) {
                  var3 = getIntersection((Set)var6, (Set)var8);
               } else {
                  var3 = getIntersection((Set)var8, (Set)var6);
               }
            } else {
               IntersectionSimilarity.TinyBag var7 = this.toBag(var6);
               IntersectionSimilarity.TinyBag var9 = this.toBag(var8);
               if (var7.uniqueElementSize() < var9.uniqueElementSize()) {
                  var3 = this.getIntersection(var7, var9);
               } else {
                  var3 = this.getIntersection(var9, var7);
               }
            }

            return new IntersectionResult(var4, var5, var3);
         }
      } else {
         throw new IllegalArgumentException("Input cannot be null");
      }
   }

   private static class BagCount {
      int count;

      private BagCount() {
         this.count = 1;
      }

      // $FF: synthetic method
      BagCount(Object var1) {
         this();
      }
   }

   private class TinyBag {
      private final Map map;

      TinyBag(int var2) {
         this.map = new HashMap(var2);
      }

      void add(Object var1) {
         IntersectionSimilarity.BagCount var2 = (IntersectionSimilarity.BagCount)this.map.get(var1);
         if (var2 == null) {
            this.map.put(var1, new IntersectionSimilarity.BagCount());
         } else {
            ++var2.count;
         }
      }

      Set entrySet() {
         return this.map.entrySet();
      }

      int getCount(Object var1) {
         IntersectionSimilarity.BagCount var2 = (IntersectionSimilarity.BagCount)this.map.get(var1);
         return var2 != null ? var2.count : 0;
      }

      int uniqueElementSize() {
         return this.map.size();
      }
   }
}
