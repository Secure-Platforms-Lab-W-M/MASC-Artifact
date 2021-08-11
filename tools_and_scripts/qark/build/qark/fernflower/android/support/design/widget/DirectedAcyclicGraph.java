package android.support.design.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pools;
import android.support.v4.util.SimpleArrayMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

final class DirectedAcyclicGraph {
   private final SimpleArrayMap mGraph = new SimpleArrayMap();
   private final Pools.Pool mListPool = new Pools.SimplePool(10);
   private final ArrayList mSortResult = new ArrayList();
   private final HashSet mSortTmpMarked = new HashSet();

   private void dfs(Object var1, ArrayList var2, HashSet var3) {
      if (!var2.contains(var1)) {
         if (var3.contains(var1)) {
            throw new RuntimeException("This graph contains cyclic dependencies");
         } else {
            var3.add(var1);
            ArrayList var6 = (ArrayList)this.mGraph.get(var1);
            if (var6 != null) {
               int var4 = 0;

               for(int var5 = var6.size(); var4 < var5; ++var4) {
                  this.dfs(var6.get(var4), var2, var3);
               }
            }

            var3.remove(var1);
            var2.add(var1);
         }
      }
   }

   @NonNull
   private ArrayList getEmptyList() {
      ArrayList var1 = (ArrayList)this.mListPool.acquire();
      return var1 == null ? new ArrayList() : var1;
   }

   private void poolList(@NonNull ArrayList var1) {
      var1.clear();
      this.mListPool.release(var1);
   }

   void addEdge(@NonNull Object var1, @NonNull Object var2) {
      if (this.mGraph.containsKey(var1) && this.mGraph.containsKey(var2)) {
         ArrayList var3 = (ArrayList)this.mGraph.get(var1);
         ArrayList var4;
         if (var3 == null) {
            var3 = this.getEmptyList();
            this.mGraph.put(var1, var3);
            var4 = var3;
         } else {
            var4 = var3;
         }

         var4.add(var2);
      } else {
         throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
      }
   }

   void addNode(@NonNull Object var1) {
      if (!this.mGraph.containsKey(var1)) {
         this.mGraph.put(var1, (Object)null);
      }
   }

   void clear() {
      int var1 = 0;

      for(int var2 = this.mGraph.size(); var1 < var2; ++var1) {
         ArrayList var3 = (ArrayList)this.mGraph.valueAt(var1);
         if (var3 != null) {
            this.poolList(var3);
         }
      }

      this.mGraph.clear();
   }

   boolean contains(@NonNull Object var1) {
      return this.mGraph.containsKey(var1);
   }

   @Nullable
   List getIncomingEdges(@NonNull Object var1) {
      return (List)this.mGraph.get(var1);
   }

   @Nullable
   List getOutgoingEdges(@NonNull Object var1) {
      ArrayList var4 = null;
      int var2 = 0;

      for(int var3 = this.mGraph.size(); var2 < var3; ++var2) {
         ArrayList var5 = (ArrayList)this.mGraph.valueAt(var2);
         if (var5 != null && var5.contains(var1)) {
            if (var4 == null) {
               var4 = new ArrayList();
            }

            var4.add(this.mGraph.keyAt(var2));
         }
      }

      return var4;
   }

   @NonNull
   ArrayList getSortedList() {
      this.mSortResult.clear();
      this.mSortTmpMarked.clear();
      int var1 = 0;

      for(int var2 = this.mGraph.size(); var1 < var2; ++var1) {
         this.dfs(this.mGraph.keyAt(var1), this.mSortResult, this.mSortTmpMarked);
      }

      return this.mSortResult;
   }

   boolean hasOutgoingEdges(@NonNull Object var1) {
      int var2 = 0;

      for(int var3 = this.mGraph.size(); var2 < var3; ++var2) {
         ArrayList var4 = (ArrayList)this.mGraph.valueAt(var2);
         if (var4 != null && var4.contains(var1)) {
            return true;
         }
      }

      return false;
   }

   int size() {
      return this.mGraph.size();
   }
}
