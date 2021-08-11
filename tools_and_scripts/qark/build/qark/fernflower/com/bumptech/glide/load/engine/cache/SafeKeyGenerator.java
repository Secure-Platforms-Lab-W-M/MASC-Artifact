package com.bumptech.glide.load.engine.cache;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SafeKeyGenerator {
   private final Pools.Pool digestPool = FactoryPools.threadSafe(10, new FactoryPools.Factory() {
      public SafeKeyGenerator.PoolableDigestContainer create() {
         try {
            SafeKeyGenerator.PoolableDigestContainer var1 = new SafeKeyGenerator.PoolableDigestContainer(MessageDigest.getInstance("SHA-256"));
            return var1;
         } catch (NoSuchAlgorithmException var2) {
            throw new RuntimeException(var2);
         }
      }
   });
   private final LruCache loadIdToSafeHash = new LruCache(1000L);

   private String calculateHexStringDigest(Key var1) {
      SafeKeyGenerator.PoolableDigestContainer var2 = (SafeKeyGenerator.PoolableDigestContainer)Preconditions.checkNotNull(this.digestPool.acquire());

      String var5;
      try {
         var1.updateDiskCacheKey(var2.messageDigest);
         var5 = Util.sha256BytesToHex(var2.messageDigest.digest());
      } finally {
         this.digestPool.release(var2);
      }

      return var5;
   }

   public String getSafeKey(Key param1) {
      // $FF: Couldn't be decompiled
   }

   private static final class PoolableDigestContainer implements FactoryPools.Poolable {
      final MessageDigest messageDigest;
      private final StateVerifier stateVerifier = StateVerifier.newInstance();

      PoolableDigestContainer(MessageDigest var1) {
         this.messageDigest = var1;
      }

      public StateVerifier getVerifier() {
         return this.stateVerifier;
      }
   }
}
