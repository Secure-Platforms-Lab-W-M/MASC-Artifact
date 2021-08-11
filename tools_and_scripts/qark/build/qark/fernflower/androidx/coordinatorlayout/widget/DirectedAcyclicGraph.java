package androidx.coordinatorlayout.widget;

import androidx.collection.SimpleArrayMap;
import androidx.core.util.Pools;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class DirectedAcyclicGraph {
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

   private ArrayList getEmptyList() {
      ArrayList var2 = (ArrayList)this.mListPool.acquire();
      ArrayList var1 = var2;
      if (var2 == null) {
         var1 = new ArrayList();
      }

      return var1;
   }

   private void poolList(ArrayList var1) {
      var1.clear();
      this.mListPool.release(var1);
   }

   public void addEdge(Object var1, Object var2) {
      if (this.mGraph.containsKey(var1) && this.mGraph.containsKey(var2)) {
         ArrayList var4 = (ArrayList)this.mGraph.get(var1);
         ArrayList var3 = var4;
         if (var4 == null) {
            var3 = this.getEmptyList();
            this.mGraph.put(var1, var3);
         }

         var3.add(var2);
      } else {
         throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
      }
   }

   public void addNode(Object var1) {
      if (!this.mGraph.containsKey(var1)) {
         this.mGraph.put(var1, (Object)null);
      }

   }

   public void clear() {
      int var1 = 0;

      for(int var2 = this.mGraph.size(); var1 < var2; ++var1) {
         ArrayList var3 = (ArrayList)this.mGraph.valueAt(var1);
         if (var3 != null) {
            this.poolList(var3);
         }
      }

      this.mGraph.clear();
   }

   public boolean contains(Object var1) {
      return this.mGraph.containsKey(var1);
   }

   public List getIncomingEdges(Object var1) {
      return (List)this.mGraph.get(var1);
   }

   public List getOutgoingEdges(Object var1) {
      ArrayList var4 = null;
      int var2 = 0;

      ArrayList var5;
      for(int var3 = this.mGraph.size(); var2 < var3; var4 = var5) {
         ArrayList var6 = (ArrayList)this.mGraph.valueAt(var2);
         var5 = var4;
         if (var6 != null) {
            var5 = var4;
            if (var6.contains(var1)) {
               var5 = var4;
               if (var4 == null) {
                  var5 = new ArrayList();
               }

               var5.add(this.mGraph.keyAt(var2));
            }
         }

         ++var2;
      }

      return var4;
   }

   public ArrayList getSortedList() {
      this.mSortResult.clear();
      this.mSortTmpMarked.clear();
      int var1 = 0;

      for(int var2 = this.mGraph.size(); var1 < var2; ++var1) {
         this.dfs(this.mGraph.keyAt(var1), this.mSortResult, this.mSortTmpMarked);
      }

      return this.mSortResult;
   }

   public boolean hasOutgoingEdges(Object var1) {
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
