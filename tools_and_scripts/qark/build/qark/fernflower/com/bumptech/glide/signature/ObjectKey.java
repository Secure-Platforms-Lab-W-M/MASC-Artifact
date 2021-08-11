package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public final class ObjectKey implements Key {
   private final Object object;

   public ObjectKey(Object var1) {
      this.object = Preconditions.checkNotNull(var1);
   }

   public boolean equals(Object var1) {
      if (var1 instanceof ObjectKey) {
         ObjectKey var2 = (ObjectKey)var1;
         return this.object.equals(var2.object);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.object.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ObjectKey{object=");
      var1.append(this.object);
      var1.append('}');
      return var1.toString();
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      var1.update(this.object.toString().getBytes(CHARSET));
   }
}
