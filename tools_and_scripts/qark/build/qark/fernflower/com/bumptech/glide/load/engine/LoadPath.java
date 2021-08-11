package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LoadPath {
   private final Class dataClass;
   private final List decodePaths;
   private final String failureMessage;
   private final Pools.Pool listPool;

   public LoadPath(Class var1, Class var2, Class var3, List var4, Pools.Pool var5) {
      this.dataClass = var1;
      this.listPool = var5;
      this.decodePaths = (List)Preconditions.checkNotEmpty((Collection)var4);
      StringBuilder var6 = new StringBuilder();
      var6.append("Failed LoadPath{");
      var6.append(var1.getSimpleName());
      var6.append("->");
      var6.append(var2.getSimpleName());
      var6.append("->");
      var6.append(var3.getSimpleName());
      var6.append("}");
      this.failureMessage = var6.toString();
   }

   private Resource loadWithExceptionList(DataRewinder var1, Options var2, int var3, int var4, DecodePath.DecodeCallback var5, List var6) throws GlideException {
      int var8 = this.decodePaths.size();
      int var7 = 0;
      Resource var9 = null;

      Resource var10;
      while(true) {
         var10 = var9;
         if (var7 >= var8) {
            break;
         }

         DecodePath var12 = (DecodePath)this.decodePaths.get(var7);

         label27: {
            try {
               var10 = var12.decode(var1, var3, var4, var2, var5);
            } catch (GlideException var11) {
               var6.add(var11);
               break label27;
            }

            var9 = var10;
         }

         if (var9 != null) {
            var10 = var9;
            break;
         }

         ++var7;
      }

      if (var10 != null) {
         return var10;
      } else {
         throw new GlideException(this.failureMessage, new ArrayList(var6));
      }
   }

   public Class getDataClass() {
      return this.dataClass;
   }

   public Resource load(DataRewinder var1, Options var2, int var3, int var4, DecodePath.DecodeCallback var5) throws GlideException {
      List var6 = (List)Preconditions.checkNotNull(this.listPool.acquire());

      Resource var9;
      try {
         var9 = this.loadWithExceptionList(var1, var2, var3, var4, var5, var6);
      } finally {
         this.listPool.release(var6);
      }

      return var9;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("LoadPath{decodePaths=");
      var1.append(Arrays.toString(this.decodePaths.toArray()));
      var1.append('}');
      return var1.toString();
   }
}
