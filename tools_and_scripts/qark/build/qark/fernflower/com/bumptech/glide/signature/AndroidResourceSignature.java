package com.bumptech.glide.signature;

import android.content.Context;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public final class AndroidResourceSignature implements Key {
   private final Key applicationVersion;
   private final int nightMode;

   private AndroidResourceSignature(int var1, Key var2) {
      this.nightMode = var1;
      this.applicationVersion = var2;
   }

   public static Key obtain(Context var0) {
      Key var1 = ApplicationVersionSignature.obtain(var0);
      return new AndroidResourceSignature(var0.getResources().getConfiguration().uiMode & 48, var1);
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof AndroidResourceSignature;
      boolean var3 = false;
      if (var2) {
         AndroidResourceSignature var4 = (AndroidResourceSignature)var1;
         var2 = var3;
         if (this.nightMode == var4.nightMode) {
            var2 = var3;
            if (this.applicationVersion.equals(var4.applicationVersion)) {
               var2 = true;
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Util.hashCode(this.applicationVersion, this.nightMode);
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      this.applicationVersion.updateDiskCacheKey(var1);
      var1.update(ByteBuffer.allocate(4).putInt(this.nightMode).array());
   }
}
