package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class MediaStoreSignature implements Key {
   private final long dateModified;
   private final String mimeType;
   private final int orientation;

   public MediaStoreSignature(String var1, long var2, int var4) {
      if (var1 == null) {
         var1 = "";
      }

      this.mimeType = var1;
      this.dateModified = var2;
      this.orientation = var4;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null) {
         if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            MediaStoreSignature var2 = (MediaStoreSignature)var1;
            if (this.dateModified != var2.dateModified) {
               return false;
            } else if (this.orientation != var2.orientation) {
               return false;
            } else {
               return this.mimeType.equals(var2.mimeType);
            }
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.mimeType.hashCode();
      long var2 = this.dateModified;
      return (var1 * 31 + (int)(var2 ^ var2 >>> 32)) * 31 + this.orientation;
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      var1.update(ByteBuffer.allocate(12).putLong(this.dateModified).putInt(this.orientation).array());
      var1.update(this.mimeType.getBytes(CHARSET));
   }
}
