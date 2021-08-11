package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;
import java.util.Map;

class EngineKey implements Key {
   private int hashCode;
   private final int height;
   private final Object model;
   private final Options options;
   private final Class resourceClass;
   private final Key signature;
   private final Class transcodeClass;
   private final Map transformations;
   private final int width;

   EngineKey(Object var1, Key var2, int var3, int var4, Map var5, Class var6, Class var7, Options var8) {
      this.model = Preconditions.checkNotNull(var1);
      this.signature = (Key)Preconditions.checkNotNull(var2, "Signature must not be null");
      this.width = var3;
      this.height = var4;
      this.transformations = (Map)Preconditions.checkNotNull(var5);
      this.resourceClass = (Class)Preconditions.checkNotNull(var6, "Resource class must not be null");
      this.transcodeClass = (Class)Preconditions.checkNotNull(var7, "Transcode class must not be null");
      this.options = (Options)Preconditions.checkNotNull(var8);
   }

   public boolean equals(Object var1) {
      if (var1 instanceof EngineKey) {
         EngineKey var2 = (EngineKey)var1;
         return this.model.equals(var2.model) && this.signature.equals(var2.signature) && this.height == var2.height && this.width == var2.width && this.transformations.equals(var2.transformations) && this.resourceClass.equals(var2.resourceClass) && this.transcodeClass.equals(var2.transcodeClass) && this.options.equals(var2.options);
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         int var1 = this.model.hashCode();
         this.hashCode = var1;
         var1 = var1 * 31 + this.signature.hashCode();
         this.hashCode = var1;
         var1 = var1 * 31 + this.width;
         this.hashCode = var1;
         var1 = var1 * 31 + this.height;
         this.hashCode = var1;
         var1 = var1 * 31 + this.transformations.hashCode();
         this.hashCode = var1;
         var1 = var1 * 31 + this.resourceClass.hashCode();
         this.hashCode = var1;
         var1 = var1 * 31 + this.transcodeClass.hashCode();
         this.hashCode = var1;
         this.hashCode = var1 * 31 + this.options.hashCode();
      }

      return this.hashCode;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("EngineKey{model=");
      var1.append(this.model);
      var1.append(", width=");
      var1.append(this.width);
      var1.append(", height=");
      var1.append(this.height);
      var1.append(", resourceClass=");
      var1.append(this.resourceClass);
      var1.append(", transcodeClass=");
      var1.append(this.transcodeClass);
      var1.append(", signature=");
      var1.append(this.signature);
      var1.append(", hashCode=");
      var1.append(this.hashCode);
      var1.append(", transformations=");
      var1.append(this.transformations);
      var1.append(", options=");
      var1.append(this.options);
      var1.append('}');
      return var1.toString();
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      throw new UnsupportedOperationException();
   }
}
