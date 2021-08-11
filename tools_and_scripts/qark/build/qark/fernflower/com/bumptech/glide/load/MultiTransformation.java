package com.bumptech.glide.load;

import android.content.Context;
import com.bumptech.glide.load.engine.Resource;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class MultiTransformation implements Transformation {
   private final Collection transformations;

   public MultiTransformation(Collection var1) {
      if (!var1.isEmpty()) {
         this.transformations = var1;
      } else {
         throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
      }
   }

   @SafeVarargs
   public MultiTransformation(Transformation... var1) {
      if (var1.length != 0) {
         this.transformations = Arrays.asList(var1);
      } else {
         throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
      }
   }

   public boolean equals(Object var1) {
      if (var1 instanceof MultiTransformation) {
         MultiTransformation var2 = (MultiTransformation)var1;
         return this.transformations.equals(var2.transformations);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.transformations.hashCode();
   }

   public Resource transform(Context var1, Resource var2, int var3, int var4) {
      Resource var5 = var2;

      Resource var6;
      for(Iterator var7 = this.transformations.iterator(); var7.hasNext(); var5 = var6) {
         var6 = ((Transformation)var7.next()).transform(var1, var5, var3, var4);
         if (var5 != null && !var5.equals(var2) && !var5.equals(var6)) {
            var5.recycle();
         }
      }

      return var5;
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      Iterator var2 = this.transformations.iterator();

      while(var2.hasNext()) {
         ((Transformation)var2.next()).updateDiskCacheKey(var1);
      }

   }
}
