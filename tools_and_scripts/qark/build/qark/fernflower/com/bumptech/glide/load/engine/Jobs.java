package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class Jobs {
   private final Map jobs = new HashMap();
   private final Map onlyCacheJobs = new HashMap();

   private Map getJobMap(boolean var1) {
      return var1 ? this.onlyCacheJobs : this.jobs;
   }

   EngineJob get(Key var1, boolean var2) {
      return (EngineJob)this.getJobMap(var2).get(var1);
   }

   Map getAll() {
      return Collections.unmodifiableMap(this.jobs);
   }

   void put(Key var1, EngineJob var2) {
      this.getJobMap(var2.onlyRetrieveFromCache()).put(var1, var2);
   }

   void removeIfCurrent(Key var1, EngineJob var2) {
      Map var3 = this.getJobMap(var2.onlyRetrieveFromCache());
      if (var2.equals(var3.get(var1))) {
         var3.remove(var1);
      }

   }
}
