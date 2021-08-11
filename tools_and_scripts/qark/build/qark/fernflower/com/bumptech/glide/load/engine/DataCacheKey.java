package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import java.security.MessageDigest;

final class DataCacheKey implements Key {
   private final Key signature;
   private final Key sourceKey;

   DataCacheKey(Key var1, Key var2) {
      this.sourceKey = var1;
      this.signature = var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof DataCacheKey;
      boolean var3 = false;
      if (var2) {
         DataCacheKey var4 = (DataCacheKey)var1;
         var2 = var3;
         if (this.sourceKey.equals(var4.sourceKey)) {
            var2 = var3;
            if (this.signature.equals(var4.signature)) {
               var2 = true;
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   Key getSourceKey() {
      return this.sourceKey;
   }

   public int hashCode() {
      return this.sourceKey.hashCode() * 31 + this.signature.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DataCacheKey{sourceKey=");
      var1.append(this.sourceKey);
      var1.append(", signature=");
      var1.append(this.signature);
      var1.append('}');
      return var1.toString();
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      this.sourceKey.updateDiskCacheKey(var1);
      this.signature.updateDiskCacheKey(var1);
   }
}
