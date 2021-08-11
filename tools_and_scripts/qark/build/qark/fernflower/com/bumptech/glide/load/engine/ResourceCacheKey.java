package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

final class ResourceCacheKey implements Key {
   private static final LruCache RESOURCE_CLASS_BYTES = new LruCache(50L);
   private final ArrayPool arrayPool;
   private final Class decodedResourceClass;
   private final int height;
   private final Options options;
   private final Key signature;
   private final Key sourceKey;
   private final Transformation transformation;
   private final int width;

   ResourceCacheKey(ArrayPool var1, Key var2, Key var3, int var4, int var5, Transformation var6, Class var7, Options var8) {
      this.arrayPool = var1;
      this.sourceKey = var2;
      this.signature = var3;
      this.width = var4;
      this.height = var5;
      this.transformation = var6;
      this.decodedResourceClass = var7;
      this.options = var8;
   }

   private byte[] getResourceClassBytes() {
      byte[] var2 = (byte[])RESOURCE_CLASS_BYTES.get(this.decodedResourceClass);
      byte[] var1 = var2;
      if (var2 == null) {
         var1 = this.decodedResourceClass.getName().getBytes(CHARSET);
         RESOURCE_CLASS_BYTES.put(this.decodedResourceClass, var1);
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof ResourceCacheKey) {
         ResourceCacheKey var2 = (ResourceCacheKey)var1;
         return this.height == var2.height && this.width == var2.width && Util.bothNullOrEqual(this.transformation, var2.transformation) && this.decodedResourceClass.equals(var2.decodedResourceClass) && this.sourceKey.equals(var2.sourceKey) && this.signature.equals(var2.signature) && this.options.equals(var2.options);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var2 = ((this.sourceKey.hashCode() * 31 + this.signature.hashCode()) * 31 + this.width) * 31 + this.height;
      Transformation var3 = this.transformation;
      int var1 = var2;
      if (var3 != null) {
         var1 = var2 * 31 + var3.hashCode();
      }

      return (var1 * 31 + this.decodedResourceClass.hashCode()) * 31 + this.options.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ResourceCacheKey{sourceKey=");
      var1.append(this.sourceKey);
      var1.append(", signature=");
      var1.append(this.signature);
      var1.append(", width=");
      var1.append(this.width);
      var1.append(", height=");
      var1.append(this.height);
      var1.append(", decodedResourceClass=");
      var1.append(this.decodedResourceClass);
      var1.append(", transformation='");
      var1.append(this.transformation);
      var1.append('\'');
      var1.append(", options=");
      var1.append(this.options);
      var1.append('}');
      return var1.toString();
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      byte[] var2 = (byte[])this.arrayPool.getExact(8, byte[].class);
      ByteBuffer.wrap(var2).putInt(this.width).putInt(this.height).array();
      this.signature.updateDiskCacheKey(var1);
      this.sourceKey.updateDiskCacheKey(var1);
      var1.update(var2);
      Transformation var3 = this.transformation;
      if (var3 != null) {
         var3.updateDiskCacheKey(var1);
      }

      this.options.updateDiskCacheKey(var1);
      var1.update(this.getResourceClassBytes());
      this.arrayPool.put(var2);
   }
}
