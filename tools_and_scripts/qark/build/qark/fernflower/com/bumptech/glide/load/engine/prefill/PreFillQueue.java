package com.bumptech.glide.load.engine.prefill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

final class PreFillQueue {
   private final Map bitmapsPerType;
   private int bitmapsRemaining;
   private int keyIndex;
   private final List keyList;

   public PreFillQueue(Map var1) {
      this.bitmapsPerType = var1;
      this.keyList = new ArrayList(var1.keySet());

      Integer var2;
      for(Iterator var3 = var1.values().iterator(); var3.hasNext(); this.bitmapsRemaining += var2) {
         var2 = (Integer)var3.next();
      }

   }

   public int getSize() {
      return this.bitmapsRemaining;
   }

   public boolean isEmpty() {
      return this.bitmapsRemaining == 0;
   }

   public PreFillType remove() {
      PreFillType var2 = (PreFillType)this.keyList.get(this.keyIndex);
      Integer var3 = (Integer)this.bitmapsPerType.get(var2);
      if (var3 == 1) {
         this.bitmapsPerType.remove(var2);
         this.keyList.remove(this.keyIndex);
      } else {
         this.bitmapsPerType.put(var2, var3 - 1);
      }

      --this.bitmapsRemaining;
      int var1;
      if (this.keyList.isEmpty()) {
         var1 = 0;
      } else {
         var1 = (this.keyIndex + 1) % this.keyList.size();
      }

      this.keyIndex = var1;
      return var2;
   }
}
