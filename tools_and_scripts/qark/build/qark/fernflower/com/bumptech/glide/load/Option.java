package com.bumptech.glide.load;

import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public final class Option {
   private static final Option.CacheKeyUpdater EMPTY_UPDATER = new Option.CacheKeyUpdater() {
      public void update(byte[] var1, Object var2, MessageDigest var3) {
      }
   };
   private final Option.CacheKeyUpdater cacheKeyUpdater;
   private final Object defaultValue;
   private final String key;
   private volatile byte[] keyBytes;

   private Option(String var1, Object var2, Option.CacheKeyUpdater var3) {
      this.key = Preconditions.checkNotEmpty(var1);
      this.defaultValue = var2;
      this.cacheKeyUpdater = (Option.CacheKeyUpdater)Preconditions.checkNotNull(var3);
   }

   public static Option disk(String var0, Option.CacheKeyUpdater var1) {
      return new Option(var0, (Object)null, var1);
   }

   public static Option disk(String var0, Object var1, Option.CacheKeyUpdater var2) {
      return new Option(var0, var1, var2);
   }

   private static Option.CacheKeyUpdater emptyUpdater() {
      return EMPTY_UPDATER;
   }

   private byte[] getKeyBytes() {
      if (this.keyBytes == null) {
         this.keyBytes = this.key.getBytes(Key.CHARSET);
      }

      return this.keyBytes;
   }

   public static Option memory(String var0) {
      return new Option(var0, (Object)null, emptyUpdater());
   }

   public static Option memory(String var0, Object var1) {
      return new Option(var0, var1, emptyUpdater());
   }

   public boolean equals(Object var1) {
      if (var1 instanceof Option) {
         Option var2 = (Option)var1;
         return this.key.equals(var2.key);
      } else {
         return false;
      }
   }

   public Object getDefaultValue() {
      return this.defaultValue;
   }

   public int hashCode() {
      return this.key.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Option{key='");
      var1.append(this.key);
      var1.append('\'');
      var1.append('}');
      return var1.toString();
   }

   public void update(Object var1, MessageDigest var2) {
      this.cacheKeyUpdater.update(this.getKeyBytes(), var1, var2);
   }

   public interface CacheKeyUpdater {
      void update(byte[] var1, Object var2, MessageDigest var3);
   }
}
