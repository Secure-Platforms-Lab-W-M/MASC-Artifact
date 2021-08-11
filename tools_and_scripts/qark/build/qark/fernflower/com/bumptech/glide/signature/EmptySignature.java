package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import java.security.MessageDigest;

public final class EmptySignature implements Key {
   private static final EmptySignature EMPTY_KEY = new EmptySignature();

   private EmptySignature() {
   }

   public static EmptySignature obtain() {
      return EMPTY_KEY;
   }

   public String toString() {
      return "EmptySignature";
   }

   public void updateDiskCacheKey(MessageDigest var1) {
   }
}
