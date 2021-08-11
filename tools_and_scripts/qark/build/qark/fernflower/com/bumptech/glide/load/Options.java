package com.bumptech.glide.load;

import androidx.collection.ArrayMap;
import com.bumptech.glide.util.CachedHashCodeArrayMap;
import java.security.MessageDigest;

public final class Options implements Key {
   private final ArrayMap values = new CachedHashCodeArrayMap();

   private static void updateDiskCacheKey(Option var0, Object var1, MessageDigest var2) {
      var0.update(var1, var2);
   }

   public boolean equals(Object var1) {
      if (var1 instanceof Options) {
         Options var2 = (Options)var1;
         return this.values.equals(var2.values);
      } else {
         return false;
      }
   }

   public Object get(Option var1) {
      return this.values.containsKey(var1) ? this.values.get(var1) : var1.getDefaultValue();
   }

   public int hashCode() {
      return this.values.hashCode();
   }

   public void putAll(Options var1) {
      this.values.putAll(var1.values);
   }

   public Options set(Option var1, Object var2) {
      this.values.put(var1, var2);
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Options{values=");
      var1.append(this.values);
      var1.append('}');
      return var1.toString();
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      for(int var2 = 0; var2 < this.values.size(); ++var2) {
         updateDiskCacheKey((Option)this.values.keyAt(var2), this.values.valueAt(var2), var1);
      }

   }
}
